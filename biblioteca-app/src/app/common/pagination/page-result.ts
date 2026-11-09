export interface PageResult<T> {
    content: T[];
    totalPages: number;
    totalElements: number;
    number: number;
    size: number;
    firts: boolean;
    last: boolean;
}