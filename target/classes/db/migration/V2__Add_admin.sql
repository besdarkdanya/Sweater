INSERT INTO users (username,password,active,filename) VALUES (
    'admin','$2y$08$56vk7zVotHc8JEKv.yWHCObfGEdJdfkLEuWioaQaxGtI7LoNXbs3C',true,'default_user_avatar.png');

INSERT INTO user_role (user_id,roles) VALUES (1,'ADMIN');