from django.shortcuts import render
from django.template import loader
from django.urls import reverse, reverse_lazy
from django.views import generic
from django.views.generic.edit import CreateView, UpdateView, DeleteView

from .models import BusinessData

def index(request):
    template_name = 'small_business/index.html'
    return render(request, template_name)

class DataDetailView(generic.DetailView):
    model = BusinessData

class DataCreateView(CreateView):
    model = BusinessData
    fields = '__all__'

class DataUpdateView(UpdateView):
    model = BusinessData
    fields = ['store', 'name', 'phone_number', 'allowed_area']

class DataDeleteView(DeleteView):
    model = BusinessData
    success_url = reverse_lazy('index')
