from django.contrib.auth.mixins import LoginRequiredMixin
from django.urls import reverse_lazy
from django.shortcuts import render
from django.views import generic
from .models import Visit


def help_page(request):
    return render(request, 'visits/help.html')


class IndexView(LoginRequiredMixin, generic.ListView):
    template_name = 'visits/index.html'
    context_object_name = 'recent_visits'

    def get_queryset(self):
        return Visit.objects.order_by('-visit_date')[:50]


class CreateView(LoginRequiredMixin, generic.CreateView):
    model = Visit
    template_name = 'visits/create.html'
    fields = ['client', 'visit_date', 'duration']
    success_url = reverse_lazy('visits:index')

    def form_valid(self, form):
        form.instance.user = self.request.user
        return super().form_valid(form)


class DetailView(LoginRequiredMixin, generic.DetailView):
    model = Visit
    template_name = 'visits/detail.html'


class UpdateView(LoginRequiredMixin, generic.UpdateView):
    model = Visit
    template_name = 'visits/update.html'
    fields = ['visit_date', 'duration']
    success_url = reverse_lazy('visits:index')


class DeleteView(LoginRequiredMixin, generic.DeleteView):
    model = Visit
    template_name = 'visits/delete.html'
    success_url = reverse_lazy('visits:index')
