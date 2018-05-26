"""
Django settings for inotracks project.
Generated by 'django-admin startproject' using Django 1.9.8.
For more information on this file, see
https://docs.djangoproject.com/en/1.9/topics/settings/
For the full list of settings and their values, see
https://docs.djangoproject.com/en/1.9/ref/settings/
"""

import os

# Build paths inside the project like this: os.path.join(BASE_DIR, ...)
BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
USE_TZ = False

# Quick-start development settings - unsuitable for production
# See https://docs.djangoproject.com/en/1.9/howto/deployment/checklist/
SECRET_KEY = 'xq_4^sexzk%kbryslfhcxygc49ws=%v&52+d-^&+(qp)voxybw'
# SECURITY WARNING: keep the secret key used in production secret

# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = True                              

ALLOWED_HOSTS = ['www.inotracks.com','inotracks.com','172.16.72.235']


# Application definition

INSTALLED_APPS = [

    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
    'users',
    'website',
    'Car',
    'API',
]

MIDDLEWARE_CLASSES = [
    'django.middleware.security.SecurityMiddleware',
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.auth.middleware.SessionAuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
]

ROOT_URLCONF = 'inotracks.urls'

TEMPLATES = [
    {
        'BACKEND': 'django.template.backends.django.DjangoTemplates',
        'DIRS': [os.path.join(BASE_DIR, 'templates')],
        'APP_DIRS': True,
        'OPTIONS': {
            'context_processors': [
                'django.template.context_processors.debug',
                'django.template.context_processors.request',
                'django.contrib.auth.context_processors.auth',
                'django.contrib.messages.context_processors.messages',
            ],
        },
    },
]

WSGI_APPLICATION = 'inotracks.wsgi.application'
AUTH_USER_MODEL = 'users.InoUser'

# Database
# https://docs.djangoproject.com/en/1.9/ref/settings/#databases

DATABASES = {

    'default': 
{
        'ENGINE': 'django.db.backends.mysql',
        'NAME': 'db_inotracks',
        'USER': 'root',
        'PASSWORD': '1715',
        'HOST': 'localhost',
        'PORT': '',
        'charset': 'utf8mb4',
}
    
}



# Password validation
# https://docs.djangoproject.com/en/1.9/ref/settings/#auth-password-validators

AUTH_PASSWORD_VALIDATORS = [
    {
        'NAME': 'django.contrib.auth.password_validation.UserAttributeSimilarityValidator',
    },
    {
        'NAME': 'django.contrib.auth.password_validation.MinimumLengthValidator',
    },
    {
        'NAME': 'django.contrib.auth.password_validation.CommonPasswordValidator',
    },
    {
        'NAME': 'django.contrib.auth.password_validation.NumericPasswordValidator',
    },
]


# Internationalization
# https://docs.djangoproject.com/en/1.9/topics/i18n/

LANGUAGE_CODE = 'en-us'


TIME_ZONE =  'Asia/Kolkata'

USE_I18N = True

USE_L10N = True




# Static files (CSS, JavaScript, Images)
# https://docs.djangoproject.com/en/1.9/howto/static-files/

STATIC_URL = '/static/'

STATICFILES_DIRS = [os.path.join(BASE_DIR, 'static'),]
