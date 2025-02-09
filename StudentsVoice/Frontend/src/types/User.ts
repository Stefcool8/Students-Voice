import {JwtPayload} from 'jwt-decode';

export interface User extends JwtPayload {
    sub?: string;
    roles: string[];
}
