from django.urls import path
from . import views

app_name = 'visits'
urlpatterns = [
    path('', views.index, name='index'),
    path('<int:visit_id>/', views.detail, name='detail'),
    path('<int:visit_id>/update/', views.update, name='update'),
    path('<int:visit_id>/updated/', views.updated, name='updated')
]
