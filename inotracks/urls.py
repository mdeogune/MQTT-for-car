from django.conf.urls import url, include
from django.contrib import admin

urlpatterns = [
    url(r'^', include('website.urls')),
    url(r'^admin/', admin.site.urls),
    url(r'^api/', include('API.urls')),
    url(r'^car/', include('Car.urls')),

]
