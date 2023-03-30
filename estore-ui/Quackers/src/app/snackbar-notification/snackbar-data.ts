export interface SnackBarData {
    message: string,
    type: SnackBarType
}

export enum SnackBarType {
    SUCCESS,
    ERROR,
    INFO
}