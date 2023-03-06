export interface Account {
    type: string;
    id: number;
    username: string;
    plainPassword: string;
    adminStatus: boolean;
}