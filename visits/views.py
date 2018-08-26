from django.http import HttpResponseRedirect
from django.shortcuts import get_object_or_404, render
from django.urls import reverse
from .models import Visit


def index(request):
    recent_visits = Visit.objects.order_by('-visit_date')[:5]
    context = {'recent_visits': recent_visits}

    return render(request, 'visits/index.html', context)


def detail(request, visit_id):
    visit = get_object_or_404(Visit, pk=visit_id)
    context = {'visit': visit}

    return render(request, 'visits/detail.html', context)


def update(request, visit_id):
    visit = get_object_or_404(Visit, pk=visit_id)
    print(request.POST['visit_date'])
    print(request.POST['duration'])
    return HttpResponseRedirect(reverse('visits:updated', args=(visit.id,)))


def updated(request, visit_id):
    visit = get_object_or_404(Visit, pk=visit_id)
    context = {'visit': visit}

    return render(request, 'visits/updated.html', context)
