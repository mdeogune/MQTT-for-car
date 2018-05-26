from django.conf.urls import url, include
from django.contrib import admin
import xlrd
from .views import *

urlpatterns = [
    url(r'^$', MapView),
    url(r'^login/$', LoginView),
    url(r'^map/$', MapView ),
    
    
    url(r'^logout/$', LogoutView ),
    

]
