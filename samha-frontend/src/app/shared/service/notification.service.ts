import {Injectable} from "@angular/core";
import notify from "devextreme/ui/notify";
import {HttpErrorResponse} from "@angular/common/http";
import {ErrorResponse} from "../../meta-model/ErrorResponse";

@Injectable()
export class NotificationService {
    public success(message: string): void{
      notify(message, 'success', 4000);
    }

    public warning(message: string): void{
      notify(message, 'warning', 4000);
    }

    public handleError(error: HttpErrorResponse): void {
      let errorResponse: ErrorResponse = error.error;
      notify(errorResponse.message, 'error', 4000);
    }

    public error(message: string) {
      notify(message, 'error', 4000);
    }
}
