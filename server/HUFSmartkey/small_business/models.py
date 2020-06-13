from django.db import models

from django.db import models

class BusinessData(models.Model):
    store = models.CharField(max_length=50)
    name = models.CharField(max_length=20)
    phone_number = models.CharField(max_length=11)
    allowedArea = models.CharField(max_length=100)
