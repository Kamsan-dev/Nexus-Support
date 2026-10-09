import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { TicketService } from '../../../service/ticket.service';
import {
  BarChartOptions,
  LineChartOptions,
  PieChartOptions,
} from '../../../core/model/chart-option';
import { ChartComponent, NgApexchartsModule } from 'ng-apexcharts';

@Component({
  selector: 'app-dashboard',
  imports: [ChartComponent, NgApexchartsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class Dashboard {
  private ticketService = inject(TicketService);

  lineChartOptions: Partial<LineChartOptions>;
  columnChartOptions: Partial<BarChartOptions>;
  donutChartOptions: Partial<PieChartOptions>;
  pieChartOptions: Partial<PieChartOptions>;

  constructor() {
    this.columnChartOptions = {
      chart: {
        type: 'bar',
        height: 500,
      },
      plotOptions: {
        bar: {
          dataLabels: {
            position: 'top',
          },
        },
      },
      dataLabels: {
        enabled: true,
        formatter: (value) => `${value} total`,
        offsetY: -20,
        style: {
          fontSize: '12px',
          colors: ['#304758'],
        },
      },
      xaxis: {
        categories: [
          'Jan',
          'Feb',
          'Mar',
          'Apr',
          'May',
          'Jun',
          'Jul',
          'Aug',
          'Sep',
          'Oct',
          'Nov',
          'Dec',
        ],
        position: 'top',
        labels: {
          offsetY: -18,
        },
        axisBorder: {
          show: false,
        },
        axisTicks: {
          show: false,
        },
        crosshairs: {
          fill: {
            type: 'gradient',
            gradient: {
              colorFrom: '#D8E3F10',
              colorTo: '#BED1E6',
              stops: [0, 100],
              opacityFrom: 0.4,
              opacityTo: 0.5,
            },
          },
        },
        tooltip: {
          enabled: true,
          offsetY: -35,
        },
      },
      fill: {
        colors: ['#06A8E0'],
      },
      yaxis: {
        title: {
          text: '$ (thousands)',
        },
        axisBorder: {
          show: false,
        },
        axisTicks: {
          show: false,
        },
        labels: {
          show: false,
          formatter: (val) => val + ' total',
        },
      },
      title: {
        text: 'Breakdown of tickets by status',
        floating: false,
        offsetY: 480,
        align: 'center',
        style: {
          color: '#444',
        },
      },
    };

    this.donutChartOptions = {
      chart: {
        height: 400,
        type: 'donut',
      },
      legend: {
        position: 'bottom',
        fontWeight: 300,
      },
      tooltip: {
        enabled: true,
        y: {
          formatter: (value: any, { series, seriesIndex, dataPointIndex, w }: any) => {
            return value;
          },
          title: {
            formatter: (serie: any) => `${serie.split(':')[0]}:`,
          },
        },
      },
    };

    this.pieChartOptions = {
      chart: {
        height: 400,
        type: 'pie',
        events: {
          click(e, chart, options) {
            console.log(e);
            console.log(chart);
            console.log(options);
          },
        },
      },
      colors: ['#F44336', '#E91E63', '9C27B0'],
      legend: {
        position: 'bottom',
        fontWeight: 300,
      },
    };

    this.lineChartOptions = {
      chart: {
        height: 350,
        type: 'line',
        zoom: {
          enabled: false,
        },
      },
      dataLabels: {
        enabled: false,
      },
      stroke: {
        curve: 'straight',
      },
      grid: {
        row: {
          colors: ['#f3f3f3', 'transparent'],
          opacity: 0.5,
        },
      },
      xaxis: {
        categories: ['Jan', 'Feb', 'Mar', 'Apr'],
      },
    };
  }
}
