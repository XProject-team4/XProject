from django.urls import path, include
from . import views
 
urlpatterns = [
    path('', views.index, name='index'),
    path('<int:pk>', views.DataDetailView.as_view(), name='detail'),
    path('create', views.DataCreateView.as_view(), name='create'),
    path('<int:pk>/update', views.DataUpdateView.as_view(), name='update'),
    path('<int:pk>/delete/', views.DataDeleteView.as_view(), name='delete'),
]