export interface SnackBarData {
    message: string,
    type: SnackBarType
}

export enum SnackBarType {
    SUCCESS = "SUCCESS",
    ERROR = "ERROR",
    INFO = "INFO"
}