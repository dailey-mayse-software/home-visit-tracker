from django.conf import settings
from django.conf.urls.static import static
from django.contrib import admin
from django.contrib.auth import views as auth_views
from django.urls import include, path
from django.views.generic import RedirectView

urlpatterns = [
    path('admin/', admin.site.urls),
    path('visits/', include('visits.urls')),
    path('', RedirectView.as_view(url='/visits/', permanent=True)),
]

# django.contrib.auth.urls:
#  accounts/ login/ [name='login']
#  accounts/ logout/ [name='logout']
#  accounts/ password_change/ [name='password_change']
#  accounts/ password_change/done/ [name='password_change_done']
#  accounts/ password_reset/ [name='password_reset']
#  accounts/ password_reset/done/ [name='password_reset_done']
#  accounts/ reset/<uidb64>/<token>/ [name='password_reset_confirm']
#  accounts/ reset/done/ [name='password_reset_complete']
urlpatterns += [
    path('accounts/login/', auth_views.LoginView.as_view(redirect_authenticated_user=True), name='login'),
    path('accounts/', include('django.contrib.auth.urls')),
]

urlpatterns += static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)
