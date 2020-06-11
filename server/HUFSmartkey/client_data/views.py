from django.shortcuts import render
from django.http import HttpResponse, JsonResponse
from django.views.decorators.csrf import csrf_exempt
from .models import ClientData
from .serializers import ClientDataSerializer
from rest_framework.parsers import JSONParser
from django.contrib.auth import authenticate

from rest_framework.decorators import api_view


#@api_view(['GET', 'POST'])
@csrf_exempt
def client_list(request, format=None):
    if request.method == 'GET': # 전체조회
        query_set = ClientData.objects.all()
        serializer = ClientDataSerializer(query_set, many=True)
        return JsonResponse(serializer.data, safe=False)
    
    elif request.method == 'POST': # 회원가입
        data = JSONParser().parse(request)
        serializer = ClientDataSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return JsonResponse(serializer.data, status=201)
        return JsonResponse(serializer.errors, status=400)

def login(request):
    if request.method == 'POST':
        data = JSONParser().parse(request)
        client_id = data['id']
        obj = ClientData.objects.get(identification=client_id)

        if data['password'] == obj.password:
            return HttpResponse(status=200)
        else:
            return HttpResponse(status=400)
        # password 넘길때 암호화 필요.
