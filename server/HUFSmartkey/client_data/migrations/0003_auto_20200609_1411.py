# Generated by Django 3.0.7 on 2020-06-09 05:11

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('client_data', '0002_auto_20200609_1238'),
    ]

    operations = [
        migrations.AlterField(
            model_name='client_data',
            name='name',
            field=models.CharField(max_length=20),
        ),
    ]