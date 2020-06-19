from django.db import models

class ClientData(models.Model):
    identification = models.CharField(max_length=10)
    password = models.CharField(max_length=10)
    phone_number = models.CharField(max_length=11)
    name = models.CharField(max_length=20)

    def __str__(self):
        return self.identification + '\'s data'