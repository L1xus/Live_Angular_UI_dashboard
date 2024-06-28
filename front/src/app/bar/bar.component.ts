import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BaseChartDirective } from 'ng2-charts';
import { DemoserviceService } from '../demoservice.service';
import DataLabelsPlugin from 'chartjs-plugin-datalabels';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';

@Component({
  selector: 'app-bar',
  standalone: true,
  imports: [CommonModule, BaseChartDirective],
  templateUrl: './bar.component.html',
  styleUrls: ['./bar.component.css']
})
export class BarComponent implements OnInit {
  @ViewChild(BaseChartDirective) chart: BaseChartDirective | undefined;
  private carsale: string = "";
  get GetCarSale(): string {
    return this.carsale;
  }
  public barChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    scales: { x: {}, y: { min: 0 } },
    plugins: {
      title: { display: true, text: 'Total Sales by Model', padding: { bottom: 30, top: 30 }, fullSize: true },
      legend: { display: true, position: 'bottom' },
      datalabels: { anchor: 'end', align: 'end' }
    }
  };
  public barChartType: ChartType = 'bar';
  public barChartPlugins = [ DataLabelsPlugin ];
  public barChartData: ChartData<'bar'> = {
    labels: [ 'Models' ],
    datasets: [ { data: [], label: '' } ]
  };

  constructor(private demoservice: DemoserviceService) { }

  ngOnInit(): void {
    this.demoservice.dataModel.subscribe(p => {
      this.carsale = p.totalSales;
      if (p && p.carModelSales) {
        this.barChartData.datasets = p.carModelSales;
        this.chart?.update();
      }
    });
  }
}
