from django.shortcuts import render

from rest_framework import viewsets

from client_data.models import client_data
from client_data.serializers import ClientDataSerializer

#youjin add code 0609_2057
from rest_framework import status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from django.http import HttpResponse, JsonResponse

# Create your views here.

# class ClientDataViewSet(viewsets.ModelViewSet):
#     queryset = client_data.objects.all()
#     serializer_class = ClientDataSerializer

#youjin add code 0609_2057 for post
@api_view(['GET', 'POST'])
def client_list(request, format=None):
    if request.method == 'GET':
        clients = client_data.objects.all()
        serializer = ClientDataSerializer(clients, many=True)
        
        return Response(serializer.data)
    
    elif request.method == 'POST':
        print("--------------------------POST---------------------")
        serializer = ClientDataSerializer(data = request.data)
        
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=201)
        return Response(serializer.errors, status=400)