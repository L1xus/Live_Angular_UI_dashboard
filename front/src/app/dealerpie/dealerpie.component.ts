import { Component, ViewChild, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BaseChartDirective } from 'ng2-charts';
import { DemoserviceService } from '../demoservice.service';
import DataLabelsPlugin from 'chartjs-plugin-datalabels';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';

@Component({
  selector: 'app-dealerpie',
  standalone: true,
  imports: [CommonModule, BaseChartDirective],
  templateUrl: './dealerpie.component.html',
  styleUrls: ['./dealerpie.component.css']
})
export class DealerpieComponent implements OnInit {
  @ViewChild(BaseChartDirective) chart: BaseChartDirective | undefined;
  public pieChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: {
      legend: { display: true, position: 'top' },
      datalabels: {
        formatter: (value, ctx) => {
          if (ctx.chart.data.labels) {
            return ctx.chart.data.labels[ctx.dataIndex];
          }
          return '';  // Ensure a return value
        },
      },
    }
  };
  public pieChartData: ChartData<'pie', number[], string | string[]> = {
    labels: [],
    datasets: [ { data: [] } ]
  };
  public pieChartType: ChartType = 'pie';
  public pieChartPlugins = [ DataLabelsPlugin ];

  constructor(private demoservice: DemoserviceService) { }

  ngOnInit(): void {
    this.demoservice.dataModel.subscribe(p => {
      if (p && p.dealerSales) {
        this.pieChartData.labels = p.dealerSales.labels;
        this.pieChartData.datasets[0].data = p.dealerSales.datasets[0].data;
        this.chart?.update();
      }
    });
  }
}
