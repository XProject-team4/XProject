from django.db import models
from django.urls import reverse

class BusinessData(models.Model):
    store = models.CharField(max_length=50)
    name = models.CharField(max_length=20, null=True)
    phone_number = models.CharField(max_length=11, null=True)
    allowed_area = models.CharField(max_length=100)
    start_time = models.CharField(max_length=50, null=True)

    def __str__(self):
        return self.store + "-" + self.name

    def get_absolute_url(self):
        return reverse("detail", kwargs={"pk": self.pk})
    
