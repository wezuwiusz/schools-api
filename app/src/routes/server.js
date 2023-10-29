export function getData(page, pageSize, text, sorting) {
    let options = sorting || { dir: null, key: null };
    return fetch(`/log/list?page=${page}&pageSize=${pageSize}&text=${text}&sortBy=${options.key}&order=${options.dir}`)
        .then((res) => res.json());
}
