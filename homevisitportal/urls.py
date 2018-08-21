from django.contrib import admin
from django.urls import include, path

urlpatterns = [
    path('timelogger', include('timelogger.urls')),
    path('admin/', admin.site.urls),
]
