import { Injectable } from '@angular/core';
import { Helloworld } from './helloworld';
import { Helloworlds } from './mock-helloworld';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';


const httpOptions = {
  headers: new HttpHeaders({ 
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': '*'

   })
};

@Injectable({
  providedIn: 'root'
})
export class HelloworldService {

  private Url = 'http://localhost:8080/helloworld/api';

  constructor(private http: HttpClient) { }


  getmsgs (): Observable<Helloworld[]> {
    return this.http.get<Helloworld[]>(this.Url)
      .pipe(
        map(res => res['_embedded'].hellowold),
        tap(_ => this.log('Success')),
        catchError(this.handleError('getmsgs', []))
      );
  }

  addHelloWorld (hello: Helloworld): Observable<Helloworld> {console.log(hello.message);
    return this.http.post<Helloworld>(this.Url, {message: hello.message}, httpOptions).pipe(
      tap((hello: Helloworld) => this.log(`Success`)),
      catchError(this.handleError<Helloworld>('ERROR'))
    );
  }






    private handleError<T> (operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
console.error("ERROR");
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption

        // Let the app keep running by returning an empty result.
        return of(result as T);
      };
    }


    private log(message: string) {
      console.log(`HelloworldService: ${message}`);
    }
}
