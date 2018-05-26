from django.shortcuts import render, redirect
from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.decorators import login_required
from django.contrib import messages
from datetime import datetime, timedelta


from Car.models import *
from users.models import *



def LoginView(request):
    template = 'login.html'
    if request.user.is_authenticated():
        return redirect('/map')
    if request.method == "POST":
        username = request.POST.get('username', None)
        password = request.POST.get('password', None)

        user = authenticate(username=username, password=password)
        print(username, password, user)
        if user is not None:
            if user.is_active:
                login(request, user)
                print(user)
                return redirect('/map')
        else:
            messages.warning(request,'Incorrect Credentials!', fail_silently=True)
            return redirect("/login")

    return render(request, template, {})

@login_required(login_url="/login")
def MapView(request):
    template = 'mapview.html'
    user = request.user
    context = {
        'name': user.name,
        'username': user.username
    }
    return render(request, template, context)






def LogoutView(request):
    logout(request)
    return redirect('/login')
