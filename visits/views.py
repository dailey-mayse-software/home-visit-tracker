from django.urls import reverse_lazy
from django.views import generic
from .models import Visit


class IndexView(generic.ListView):
    context_object_name = 'recent_visits'
    template_name = 'visits/index.html'

    def get_queryset(self):
        return Visit.objects.order_by('-visit_date')[:50]


class CreateView(generic.CreateView):
    model = Visit
    template_name = 'visits/create.html'
    fields = ['user', 'client', 'visit_date', 'duration']
    success_url = reverse_lazy('visits:index')


class DetailView(generic.DetailView):
    model = Visit
    template_name = 'visits/detail.html'


class UpdateView(generic.UpdateView):
    model = Visit
    template_name = 'visits/update.html'
    fields = ['visit_date', 'duration']
    success_url = reverse_lazy('visits:index')


class DeleteView(generic.DeleteView):
    model = Visit
    template_name = 'visits/delete.html'
    success_url = reverse_lazy('visits:index')
