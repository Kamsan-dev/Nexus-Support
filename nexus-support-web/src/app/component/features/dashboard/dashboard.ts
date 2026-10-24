import { ChangeDetectionStrategy, Component, effect, inject, signal, viewChild } from '@angular/core';
import { ChartComponent, NgApexchartsModule } from 'ng-apexcharts';
import { BarChartOptions, LineChartOptions, PieChartOptions } from '../../../core/model/chart-option';
import { TicketStatusColor, TicketStatusLabel } from '../../../enum/ticket-status';
import { TicketTypeLabel } from '../../../enum/ticket-type';
import { DashboardStore } from '../../../store/dashbord.store';
import { CommonModule, DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  imports: [ChartComponent, NgApexchartsModule, DatePipe, CommonModule, RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Dashboard {
  protected readonly store = inject(DashboardStore);

  lineChartOptions = signal<Partial<LineChartOptions> | undefined>(undefined);
  columnChartOptions = signal<Partial<BarChartOptions> | undefined>(undefined);
  donutChartOptions = signal<Partial<PieChartOptions> | undefined>(undefined);
  pieChartOptions = signal<Partial<PieChartOptions> | undefined>(undefined);

  protected readonly pieChart = viewChild<ChartComponent>('pieChart');

  readonly statusLabels = TicketStatusLabel;
  readonly typeLabels = TicketTypeLabel;
  readonly ticketStatusColors = TicketStatusColor;

  constructor() {
    effect(() => {
      this.listenToLoadingStats();
    });
    this.columnChartOptions.set({
      chart: {
        type: 'bar',
        height: 400,
        toolbar: {
          show: false,
        },
        animations: {
          enabled: true,
          speed: 800,
        },
      },
      series: [],
      plotOptions: {
        bar: {
          horizontal: true,
          borderRadius: 4,
          borderRadiusApplication: 'end',
          barHeight: '65%',
          dataLabels: {
            position: 'center',
          },
        },
      },
      dataLabels: {
        enabled: true,
        formatter: (value: number) => `${value}`,
        style: {
          fontSize: '12px',
          colors: ['#FFFFFF'],
        },
      },
      xaxis: {
        categories: [],
        title: {
          text: 'Nombre de tickets',
        },
        min: 0,
      },
      yaxis: {
        labels: {
          style: {
            fontSize: '12px',
          },
        },
      },
      grid: {
        borderColor: '#E5E7EB',
        strokeDashArray: 4,
      },
      legend: {
        show: false,
      },
      tooltip: {
        y: {
          formatter: (value: number) => `${value} ticket(s)`,
        },
      },
    });

    this.donutChartOptions.set({
      chart: {
        height: 400,
        type: 'donut',
      },
      labels: [],
      series: [],
      legend: {
        position: 'bottom',
        fontWeight: 300,
      },
      dataLabels: {
        enabled: true,
        formatter: (percentage: number) => `${percentage.toFixed(1)}%`,
      },
      tooltip: {
        enabled: true,
        y: {
          formatter: (value: number) => `${value} ticket(s)`,
        },
      },
      plotOptions: {
        pie: {
          donut: {
            size: '65%',
            labels: {
              show: true,
              total: {
                show: true,
                label: 'Total tickets',
                formatter: (w: any) => w.globals.seriesTotals.reduce((total: number, value: number) => total + value, 0).toString(),
              },
            },
          },
        },
      },
    });

    this.pieChartOptions.set({
      chart: {
        height: 400,
        type: 'pie',
        animations: {
          enabled: true,
          easing: 'easeinout',
          speed: 1500,
          dynamicAnimation: {
            enabled: true,
            speed: 1500,
          },
        },
      },
      colors: ['#F44336', '#E91E63', '#9C27B0', '#673AB7', '#3F51B5', '#2196F3', '#009688'],
      legend: {
        position: 'bottom',
        fontWeight: 300,
      },
      responsive: [
        {
          breakpoint: 480,
          options: {
            chart: {
              height: 300,
            },
            legend: {
              position: 'bottom',
            },
          },
        },
      ],
    });

    this.lineChartOptions.set({
      chart: {
        height: 350,
        type: 'line',
        toolbar: {
          show: false,
        },
        zoom: {
          enabled: false,
        },
        animations: {
          enabled: true,
          speed: 800,
        },
      },
      series: [],
      colors: ['#F59E0B', '#10B981'],
      dataLabels: {
        enabled: false,
      },
      stroke: {
        curve: 'smooth',
        width: 3,
      },
      markers: {
        size: 4,
        hover: {
          size: 6,
        },
      },
      grid: {
        borderColor: '#E5E7EB',
        strokeDashArray: 4,
      },
      legend: {
        position: 'top',
        horizontalAlign: 'right',
        fontWeight: 300,
      },
      xaxis: {
        categories: [],
        labels: {
          rotate: -45,
        },
      },
      yaxis: {
        min: 0,
        forceNiceScale: true,
        title: {
          text: 'Number of tickets',
        },
      },
      tooltip: {
        shared: true,
        intersect: false,
      },
    });
  }

  ngOnInit() {
    this.store.loadStats();
  }

  private listenToLoadingStats(): void {
    if (this.store.statsRetrieved() && this.store.stats()) {
      this.pieChartOptions.update((options) => ({
        ...options,
        labels: this.store.stats()!.ticketsByStatus.map(({ status }) => this.statusLabels[status]),
        series: this.store.stats()!.ticketsByStatus.map(({ ticketCount }) => ticketCount),
      }));

      this.donutChartOptions.update((options) => ({
        ...options,
        labels: this.store.stats()!.ticketsByType.map(({ type }) => this.typeLabels[type]),
        series: this.store.stats()!.ticketsByType.map(({ ticketCount }) => ticketCount),
      }));

      this.lineChartOptions.update((options) => ({
        ...options,
        series: [
          {
            name: 'New tickets',
            data: this.store.stats()!.weeklyTrends.map(({ newCount }) => newCount),
          },
          {
            name: 'Completed tickets',
            data: this.store.stats()!.weeklyTrends.map(({ completedCount }) => completedCount),
          },
        ],
        xaxis: {
          ...options!.xaxis,
          categories: this.store.stats()!.weeklyTrends.map(({ weekStart }) =>
            new Date(`${weekStart}T12:00:00`).toLocaleDateString('fr-FR', {
              day: '2-digit',
              month: 'short',
            }),
          ),
        },
      }));

      this.columnChartOptions.update((options) => ({
        ...options,
        series: [
          {
            name: 'Tickets',
            data: this.store.stats()!.ticketsByStatus.map(({ ticketCount }) => ticketCount),
          },
        ],
        xaxis: {
          ...options!.xaxis,
          categories: this.store.stats()!.ticketsByStatus.map(({ status }) => this.statusLabels[status]),
        },
      }));
    }
  }
}
