# Тестовое задание

## REST API
### Аунтефикация
```
POST <server_url>/signup
{
    "login": "user_login"
    "password": "user_pass"
}
```
```
POST <server_url>/signin
{
    "login": "user_login"
    "password": "user_pass"
}
```
# Операция отправление денег
```
POST <server_url>/money
{
    "to": "user_b_login"
    "amount": 500
}
```
# Операция Получение баланса
```
GET <server_url>/money
```

