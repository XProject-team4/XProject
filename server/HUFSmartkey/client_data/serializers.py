from rest_framework import serializers
from client_data.models import client_data

class ClientDataSerializer(serializers.ModelSerializer):
	class Meta:
		model = client_data
		fields = ('identification','password','phone_number','name',)
