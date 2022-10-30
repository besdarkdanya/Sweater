INSERT INTO users (username,password,active,avatar_filename,background_filename,description) VALUES (
    'admin','$2y$08$56vk7zVotHc8JEKv.yWHCObfGEdJdfkLEuWioaQaxGtI7LoNXbs3C',
    true,'default_user_avatar.png','default_background_image.jpeg','This is admins profile description');

INSERT INTO user_role (user_id,roles) VALUES (1,'ADMIN');