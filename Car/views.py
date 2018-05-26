from django.shortcuts import render
from django.shortcuts import render, Http404, HttpResponse
import json
# import pytz
from datetime import datetime, date, time
from .models import *

def updateShift(bus):
    try:
        timeTable = TimeTable.objects.get(bus=bus)
        stops = timeTable.stoppage.all()
        n = len(stops)
        source, destination = stops[0].location.place_name, stops[n-1].location.place_name
        today_min = datetime.combine(date.today(), time.min)
        today_max = datetime.combine(date.today(), time.max)
        bus_number = bus.bus_number
        todayslocs = location.objects.filter(bus_number=bus_number,
                                    time_recorded__range=(today_min, today_max))
        shifts = 0
        for location in todayslocs:
            place = location.place_name
            if place == source or place==destination:
                print(place)
                shifts += 1
        if shifts:
            shifts -= 1
        bus.shifts = shifts
        bus.save()
        print(shifts ,source, destination)
    except Exception as e:
        print(e)

def RefreshShifts(request):
    user = request.user
    if user.is_authenticated():
        buses = Bus.objects.filter(owner=user)
        for bus in buses:
            updateShift(bus)

    response_data = {
        'success':'true',
    }
    return HttpResponse(
        json.dumps(response_data),
        content_type = "application/json"
        )
