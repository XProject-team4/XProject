from django.urls import path, include
from . import views
 
urlpatterns = [
    path('', views.client_list), 
    path('login', views.login),
    path('door_open', views.doorOpen),
]
