# Generated by Django 3.0.3 on 2020-06-20 14:32

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('small_business', '0005_auto_20200620_2304'),
    ]

    operations = [
        migrations.AddField(
            model_name='businessdata',
            name='start_time',
            field=models.CharField(max_length=50, null=True),
        ),
    ]
