from django.db import models
# from django.utils import timezone
from datetime import datetime
from django.utils.translation import ugettext_lazy as _
import requests
from users.models import *


class Location(models.Model):
    v_number = models.CharField(max_length=100)
    latitude = models.FloatField(blank=True,null=True)
    longitude = models.FloatField(blank=True,null=True)
    place_name = models.TextField(null=True, blank=True)
    known_location = models.BooleanField(default=False)
    time_recorded = models.DateTimeField(default=datetime.now)

    def __str__(self):
        return "%s" % self.time_recorded

    def get_place(self):
        return "%s" %self.place_name

    def save(self, *args, **kwargs):
        # if not self.known_location:
        #     base = "http://maps.googleapis.com/maps/api/geocode/json?"
        #     params = "latlng={lat},{lon}&sensor={sen}".format(
        #         lat = self.latitude,
        #         lon = self.longitude,
        #         sen = 'true',
        #         )

        #     url = "{base}{params}".format(base=base, params=params)
        #     try:
        #         response = requests.get(url)
        #         self.place_name = response.json()['results'][0]['formatted_address']
        #     except Exception as e:
        #         self.place_name = response.json() #will help to sort out the exception
        super(Location, self).save(*args, **kwargs)


class CarParameter(models.Model):
    v_number = models.CharField(max_length=100)
    speed = models.FloatField()
    fuel = models.FloatField()
    battery = models.FloatField(blank=True,null=True)
    distance = models.FloatField()
    time_recorded = models.DateTimeField(default=datetime.now)

    def __str__(self):
        return "%s" % (self.time_recorded)


class Car(models.Model):
    owner = models.ForeignKey(InoUser)
    driver = models.OneToOneField(InoDriver)
    v_number = models.CharField(max_length=100)
    location = models.OneToOneField(Location)
    running_status = models.BooleanField(default=False)
    shifts = models.IntegerField(default=0)
    parameters = models.OneToOneField(CarParameter)

    def __str__(self):
        return "%s" % self.v_number

    class Meta:
        verbose_name = _('Car')
        verbose_name_plural = _('Car')





