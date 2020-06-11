from django.urls import path, include

# youjin add code 0609_2059
from rest_framework.urlpatterns import format_suffix_patterns
from client_data import views

app_name = 'client_data'

urlpatterns = [
    path('', include('rest_framework.urls',namespace= 'rest_framework_category')),
    path('clients', views.client_list), # youjin add code 0609_2059
]
# youjin add code 0609_2059
urlpatterns = format_suffix_patterns(urlpatterns)