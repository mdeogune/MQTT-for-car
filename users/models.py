from datetime import date
from django.db import models
from django.contrib.auth.models import PermissionsMixin, AbstractUser
from django.contrib.auth.base_user import AbstractBaseUser
from django.utils.translation import ugettext_lazy as _

from .manager import UserManager


user_choices = [
    (None,'type'),
    ('Owner', 'Owner'),
    ('Employee', 'Employee'),
]

class InoUser(AbstractBaseUser, PermissionsMixin):
    username = models.CharField(_('username'), max_length=130, unique=True)
    user_type = models.CharField(max_length=10,choices=user_choices,)
    name = models.CharField(_('full name'), max_length=130)
    phone_number = models.BigIntegerField(unique=True)
    is_active = models.BooleanField(_('is_active'), default=True)
    is_staff = models.BooleanField(_('is_staff'), default=False)

    objects = UserManager()

    USERNAME_FIELD = 'username'
    REQUIRED_FIELDS = ['name', 'phone_number']

    class Meta:
        ordering = ('username',)
        verbose_name = _('User')
        verbose_name_plural = _('Users')

    def get_short_name(self):
        return self.username


class InoDriver(models.Model):
    name = models.CharField(_('full name'), max_length=130, blank=True)
    phone_number = models.BigIntegerField(null=True,blank=True)
    driving_licence = models.CharField(max_length=150, null=True, blank=True)

    class Meta:
        verbose_name = _('Driver')
        verbose_name_plural = _('Drivers')

    def __str__(self):
        return self.name
