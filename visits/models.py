from django.contrib.auth import get_user_model
from django.db import models


class Client(models.Model):
    first_name = models.CharField(max_length=500)
    last_name = models.CharField(max_length=500)
    city = models.CharField(max_length=500)
    county = models.CharField(max_length=500)

    def __str__(self):
        return f'{self.first_name} {self.last_name}'


class Visit(models.Model):
    user = models.ForeignKey(get_user_model(), on_delete=models.CASCADE)
    client = models.ForeignKey(Client, on_delete=models.CASCADE)
    visit_date = models.DateTimeField()
    duration = models.IntegerField()

    def __str__(self):
        return f'{self.client} on {self.visit_date}'
