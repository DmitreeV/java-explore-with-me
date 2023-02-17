# java-explore-with-me
Ссылка на PR итерация 1 https://github.com/DmitreeV/java-explore-with-me/pull/3

Добавлена новая функциональность - возможность комментировать события.
REST API фичи комментарии (включена в основной сервис):
Приватный API:
	◦	POST /users/{userId}/comments/{eventId} создание комментария к событию.	 
	◦	PATCH /users/{userId}/comments/{commentId} обновление комментария .
	◦	DELETE /users/{userId}/comments/{commentId} удаление комментария пользователем.
	◦	GET /users/{userId}/comments?from={from}&size={size} получение списка комментариев пользователем.
Публичный API:
	◦	GET /events/{eventId}/comments?from={from}&size={size} получение списка комментариев для события.
Административный API:
	◦	DELETE /admin/comments/{commentId} удаление комментария администратором.
