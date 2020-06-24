from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt
from .models import ClientData
from .serializers import ClientDataSerializer
from rest_framework.parsers import JSONParser
from django.contrib.auth import authenticate
from django.views.decorators.csrf import csrf_exempt
from django.utils.decorators import method_decorator
import time
import sqlite3
import serial 

# For arduino serial port 
arduino = serial.Serial(
    port='/dev/ttyACM0',
    baudrate=9600,
)
print('Connected Serial Port is ' + arduino.portstr)


@csrf_exempt
def client_list(request, format=None): # app --(identification, password, phone_number, name)--> server
    if request.method == 'GET': # 전체조회
        query_set = ClientData.objects.all()
        serializer = ClientDataSerializer(query_set, many=True)
        return JsonResponse(serializer.data, safe=False)
    
    elif request.method == 'POST': # 회원가입_test완료
        identification = request.POST.get("identification", "")
        password = request.POST.get("password", "")
        phone_number = request.POST.get("phone_number", "")
        name = request.POST.get("name", "")

        print('identification = ' + identification + 'password = ' + password + 'phone_number = ' + phone_number + 'name= ' + name) # 서버쪽 터미널에 띄움
        myuser = ClientData.objects.filter(identification=identification)

        if myuser: # db에 저장되어있으면 -> id중복
            print("duplicated id, signUp failed") # for server debugging
            return JsonResponse({'code':'400', 'msg':'duplicated id'}, status=400)
        else: # new client면 -> db저장
            form = ClientData(identification=identification, password=password, phone_number=phone_number, name=name)
            form.save()
            print("signUp success") # for server debugging
            return JsonResponse({'code':'201', 'msg':'signup success'}, status=201) # app으로 보내는 msg
        
@method_decorator(csrf_exempt, name='dispatch')
def login(request, format=None): # app --(identification, password)--> server --(allowed_area)--> app
    if request.method == "GET": 
        return render(request, 'client_data/login.html')

    elif request.method == 'POST':
        identification = request.POST.get("identification", "")
        password = request.POST.get("password", "")
        myuser = ClientData.objects.filter(identification=identification, password=password)

        print("identification = " + identification + " password" + password)

        if myuser:
            print("login success")

        obj = ClientData.objects.get(identification=identification)
        phone_number = obj.phone_number
        name = obj.name

        print("identification = " + identification + " password" + password)
        print("phone:" + phone_number + "name:" + name)

        if myuser:
            print("login success")
            try:  
                con = sqlite3.connect("db.sqlite3")
                cursor = con.cursor()
                db = cursor.execute("SELECT allowed_area FROM small_business_businessdata WHERE phone_number='%s' AND name='%s'" %(phone_number, name)).fetchall()[0][0]
                return JsonResponse({'code':'201', 'msg':'login success', 'allowed_area' : db}, status=201)
            except IndexError: # business_data db에 없을 때 -> guest일때
                return JsonResponse({'code':'201', 'msg':'login success', 'allowed_area' : "nothing:nothing,nothing:nothing"}, status=201)
        else:
            print("login failed")
            return JsonResponse({'code':'400', 'msg':'login failed'}, status=400)

        # password 넘길때 암호화 필요. -> 추가하기

@csrf_exempt
def door_open(request, format=None): # app --(id, uuid)--> server
    if request.method == "GET":
        return render(request, 'client_data/login.html')
    if request.method == 'POST':
        id = request.POST.get("id", "")
        uuid = request.POST.get("uuid", "")

        print("<door_open> id = " + id + " uuid" + uuid)
        if(int(id) == 0): # 사업자 -> 바로 문 열어준다.
            # from .ctr_servo import run_servo
            # run_servo(1) # run servo Motor
            arduino.write([1])
            data = arduino.read()
            print(data)

            return JsonResponse({'code':'201', 'msg':'true'}, status=201)
        elif(int(id) > 0): # guest일 때 -> 현재시각과 service start 한 시간 비교
            now = round(time.time())
            print("now time is:" + str(now))
            con = sqlite3.connect("db.sqlite3")
            cursor = con.cursor()
            start_time = cursor.execute("SELECT start_time FROM small_business_businessdata WHERE id='%d'" %(int(id))).fetchall()[0][0]
            if(now - int(start_time) >= 7200): # service time 이 두시간 이상일 때
                db = cursor.execute("DELETE FROM small_business_businessdata WHERE id='%d'" %(int(id)))
                print("service time done")
                return JsonResponse({'code':'201', 'msg':'false'}, status=201) # service time done
            else:
                # from .ctr_servo import run_servo
                # run_servo(1) # run servo Motor
                arduino.write([1])
                data = arduino.read()
                print(data)
                return JsonResponse({'code':'201', 'msg':'true'}, status=201) # door open
        else :
            return JsonResponse({'code':'400', 'msg':'door not open'}, status=400)

@csrf_exempt
def first_qr_scan(request, format=None): # app --(store, allowed_data)--> server --(id)--> app
    if request.method == 'POST':
        store = request.POST.get("store", "")
        allowed_area = request.POST.get("allowed_area", "")

        print("from app) store: " + store + ", allowed_area: " + allowed_area)

        start_time = str(round(time.time()))
        print("start time is : " + start_time)

        con = sqlite3.connect("db.sqlite3")
        cursor = con.cursor()
        db = cursor.execute("INSERT INTO small_business_businessdata (store, allowed_area, start_time) VALUES ('%s', '%s', '%s')" %(store, allowed_area, start_time))
        id = cursor.execute("SELECT id FROM small_business_businessdata WHERE start_time='%s'" %(start_time)).fetchall()[0][0]
        con.commit()
        con.close()
        print("db insert result id:" + str(id))

        if (id >= 0):
            return JsonResponse({'code':'201', 'msg': str(id)}, status=201)
        else:
            return JsonResponse({'code':'400', 'msg':'store into db as guest failed'}, status=400)
