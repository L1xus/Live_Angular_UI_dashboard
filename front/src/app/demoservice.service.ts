import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import * as Stomp from 'stompjs';
import SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class DemoserviceService {
  apiUrl: string = 'http://localhost:8080/carsales/last30days';
  private httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };
  private _dataModel: BehaviorSubject<any> = new BehaviorSubject<any>({});
  private dataStore: { data: any } = { data: {} };
  readonly dataModel = this._dataModel.asObservable();
  streamURL: string = 'http://localhost:8080/websocket';
  client: any;
  streamMessage: any;

  constructor(private http: HttpClient) {
    this.getCarSalesData();
    this.streamCarSalesData();
  }

  getCarSalesData() {
    this.http.get(`${this.apiUrl}`).subscribe(data => {
      this.dataStore.data = data;
      this._dataModel.next(Object.assign({}, this.dataStore).data);
    });
  }

  streamCarSalesData() {
    let ws = new SockJS(this.streamURL);
    this.client = Stomp.over(ws);
    this.client.connect({}, (frame: any) => {
      this.client.subscribe('/cars', (message: any) => {
        if (message.body) {
          this.streamMessage = message.body;
          this.dataStore.data = JSON.parse(this.streamMessage);
          this._dataModel.next(Object.assign({}, this.dataStore).data);
        }
      });
    });
  }
}
