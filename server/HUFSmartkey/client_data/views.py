from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt
from .models import ClientData
from .serializers import ClientDataSerializer
from rest_framework.parsers import JSONParser
from django.contrib.auth import authenticate
from django.views.decorators.csrf import csrf_exempt
from django.utils.decorators import method_decorator

@csrf_exempt
def client_list(request, format=None):
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
def login(request, format=None): # test완료
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

            import sqlite3
            con = sqlite3.connect("db.sqlite3")
            cursor = con.cursor()
            db = cursor.execute("SELECT allowed_area FROM small_business_businessdata WHERE phone_number='%s' AND name='%s'" %(phone_number, name)).fetchall()[0][0]
            
            return JsonResponse({'code':'201', 'msg':'login success', 'allowed_area' : db}, status=201)
        else:
            print("login failed")
            return JsonResponse({'code':'400', 'msg':'login failed'}, status=400)

        # password 넘길때 암호화 필요. -> 추가하기
