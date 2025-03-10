-- create joint table between gebio_user and board
create table gebio_user_board(
    user_id uuid not null,
    board_id uuid not null,
    constraint pk_gebio_user_board primary key (user_id, board_id),
    constraint fk_gebio_user_board_user foreign key (user_id) references gebio_user(user_id),
    constraint fk_gebio_user_board_board foreign key (board_id) references board(board_id)
);

-- insert present board owner inside joint table
insert into gebio_user_board (user_id, board_id)
select board.owner_id, board.board_id from board