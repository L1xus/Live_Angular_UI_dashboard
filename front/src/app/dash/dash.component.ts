import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card'; // Import Angular Material card module
import { BarComponent } from '../bar/bar.component'; // Import your chart components
import { DealerpieComponent } from '../dealerpie/dealerpie.component';
import { StatepieComponent } from '../statepie/statepie.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule, BarComponent, DealerpieComponent, StatepieComponent],
  templateUrl: './dash.component.html',
  styleUrls: ['./dash.component.css']
})
export class DashboardComponent {}
