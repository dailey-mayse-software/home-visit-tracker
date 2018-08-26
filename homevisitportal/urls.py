from django.contrib import admin
from django.urls import include, path

urlpatterns = [
    path('visits/', include('visits.urls')),
    path('admin/', admin.site.urls),
]
