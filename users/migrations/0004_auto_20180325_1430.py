# -*- coding: utf-8 -*-
# Generated by Django 1.11.1 on 2018-03-25 14:30
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('users', '0003_auto_20180325_1336'),
    ]

    operations = [
        migrations.AlterField(
            model_name='inodriver',
            name='phone_number',
            field=models.BigIntegerField(blank=True, null=True),
        ),
    ]
