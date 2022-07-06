INSERT INTO "BookApp".public.file_upload_response(file_name, content_type, url)
VALUES ('avatar1.svg', 'image/svg', 'http://localhost:8080/api/v1/files/avatar1.svg'),
       ('avatar2.svg', 'image/svg', 'http://localhost:8080/api/v1/files/avatar2.svg'),
       ('avatar3.svg', 'image/svg', 'http://localhost:8080/api/v1/files/avatar3.svg'),
       ('book1.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book1.jpeg'),
       ('book2.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book2.jpeg'),
       ('book3.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book3.jpeg'),
       ('book4.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book4.jpeg'),
       ('book5.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book5.jpeg'),
       ('book6.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book6.jpeg'),
       ('book7.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book7.jpeg'),
       ('book8.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book8.jpeg'),
       ('book9.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book9.jpeg'),
       ('book10.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book10.jpeg'),
       ('book11.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book11.jpeg'),
       ('book12.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book12.jpeg'),
       ('book13.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book13.jpeg'),
       ('book14.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book14.jpeg'),
       ('book15.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book15.jpeg'),
       ('book16.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book16.jpeg'),
       ('book17.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book17.jpeg'),
       ('book18.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book18.jpeg'),
       ('book19.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book19.jpeg'),
       ('book20.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book20.jpeg'),
       ('book21.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book21.jpeg'),
       ('book22.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book22.jpeg'),
       ('book23.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book23.jpeg'),
       ('book24.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book24.jpeg'),
       ('book25.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book25.jpeg'),
       ('book26.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book26.jpeg'),
       ('book27.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book27.jpeg'),
       ('book28.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book28.jpeg'),
       ('book29.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book29.jpeg'),
       ('book30.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book30.jpeg'),
       ('book31.jpeg', 'image/jpeg', 'http://localhost:8080/api/v1/files/book31.jpeg');

INSERT INTO users (first_name, last_name, city, email, username, password, enabled, avatar_file_name)
VALUES ('Michiel', 'Oosterhuis', 'Utrecht', 'michiel@gmail.com', 'michiel',
        '$2a$12$p9j6VtrvzutDaIV7L2XhROBqhg5A/Vx6ybta3e.r9TZJ.9V1SkaDe', true, 'avatar1.svg'),
       ('Christiaan', 'Kreling', 'Amersfoort', 'chris@gmail.com', 'christiaan',
        '$2a$12$p9j6VtrvzutDaIV7L2XhROBqhg5A/Vx6ybta3e.r9TZJ.9V1SkaDe', true, 'avatar2.svg'),
       ('Leonie', 'Meulman', 'Utrecht', 'leonie@gmail.com', 'leonie',
        '$2a$12$p9j6VtrvzutDaIV7L2XhROBqhg5A/Vx6ybta3e.r9TZJ.9V1SkaDe', true, 'avatar3.svg');

INSERT INTO books (author, is_available, language, title, transaction_type, year, book_cover_file_name, owner_id)
VALUES ('Pien Wekking', false, 'DUTCH', 'Pien laat haar eten zien', 'EXCHANGE_FOR_BOOK', 2022, 'book1.jpeg', 'michiel'),
       ('Lucinda Riley', true, 'DUTCH', 'De geheimen van de kostschool', 'EXCHANGE_FOR_BOOK', 2022, 'book2.jpeg',
        'michiel'),
       ('Kelly Weekers', false, 'DUTCH', 'De Kracht van keuze', 'EXCHANGE_FOR_BOOK', 2022, 'book3.jpeg', 'michiel'),
       ('Karin Slaughter', true, 'DUTCH', 'Gewetenloos', 'EXCHANGE_FOR_BOOK', 2022, 'book4.jpeg', 'michiel'),
       ('Hanneke de Zoete', true, 'DUTCH', 'De Zoete Zusjes - Het grote Zoete Zusjes vakantieboek 2',
        'EXCHANGE_FOR_CAKE', 2022, 'book5.jpeg', 'michiel'),
       ('Delia Owens', false, 'DUTCH', 'Daar waar de rivierkreeften zingen', 'EXCHANGE_FOR_CAKE', 2021, 'book6.jpeg',
        'michiel'),
       ('Marcel van Roosmalen', true, 'DUTCH', 'Totaal', 'GIFT', 2022, 'book7.jpeg', 'michiel'),
       ('Sofie van Daalen Buissant des Amorie', true, 'DUTCH', 'Lekker & simpel 10 jaar', 'GIFT', 2022, 'book8.jpeg',
        'michiel'),
       ('Lisa Jewell', true, 'DUTCH', 'Toen was ze weg', 'GIFT', 2021, 'book9.jpeg', 'michiel'),

       ('Thijs Launspach', false, 'DUTCH', 'Je bent al genoeg', 'EXCHANGE_FOR_BOOK', 2022, 'book10.jpeg', 'christiaan'),
       ('Michael Pilarczyk', true, 'DUTCH', 'Master Your Mindset', 'EXCHANGE_FOR_BOOK', 2020, 'book11.jpeg',
        'christiaan'),
       ('Corina Bomann', true, 'DUTCH', 'Waldfriede 1 - Gloriedagen', 'EXCHANGE_FOR_BOOK', 2022, 'book12.jpeg',
        'christiaan'),
       ('Delia Owens', false, 'DUTCH', 'Daar waar de rivierkreeften zingen', 'EXCHANGE_FOR_CAKE', 2020, 'book13.jpeg',
        'christiaan'),
       ('Marion Pauw', false, 'DUTCH', 'Vogeleiland', 'GIFT', 2021, 'book14.jpeg', 'christiaan'),

       ('Michelle Frances', true, 'DUTCH', 'Zijn nieuwe vriendin', 'EXCHANGE_FOR_BOOK', 2022, 'book15.jpeg', 'leonie'),
       ('Haemin Sunim', true, 'DUTCH', 'Dingen die je alleen ziet als je er de tijd voor neemt', 'EXCHANGE_FOR_BOOK',
        2017, 'book16.jpeg', 'leonie'),
       ('Suzanne Vermeer', true, 'DUTCH', 'Roadtrip', 'EXCHANGE_FOR_BOOK', 2022, 'book17.jpeg', 'leonie'),
       ('Elke Wiss', true, 'DUTCH', 'Socrates op sneakers', 'EXCHANGE_FOR_CAKE', 2020, 'book18.jpeg', 'leonie'),
       ('Hidde de Vries', true, 'DUTCH', 'Work smart play smart.nl', 'EXCHANGE_FOR_CAKE', 2021, 'book19.jpeg',
        'leonie'),
       ('Joris Luyendijk', false, 'DUTCH', 'De zeven vinkjes', 'GIFT', 2022, 'book20.jpeg', 'leonie'),
       ('Isabel Allende', true, 'DUTCH', 'Violeta', 'GIFT', 2022, 'book21.jpeg', 'leonie'),


       ('Colleen Hoover', true, 'ENGLISH', 'It Ends With Us', 'EXCHANGE_FOR_BOOK', 2016, 'book22.jpeg', 'michiel'),
       ('Colleen Hoover', true, 'ENGLISH', 'Ugly Love', 'EXCHANGE_FOR_BOOK', 2014, 'book23.jpeg', 'michiel'),

       ('The Cairo Review of Global Affairs', true, 'ENGLISH', 'Trump''s World', 'EXCHANGE_FOR_BOOK', 2017,
        'book24.jpeg', 'christiaan'),
       ('Jenny Han', true, 'ENGLISH', 'The Summer I Turned Pretty', 'EXCHANGE_FOR_CAKE', 2010, 'book25.jpeg',
        'christiaan'),
       ('Alice Oseman', true, 'ENGLISH', 'Heartstopper Volume One', 'GIFT', 2019, 'book26.jpeg', 'christiaan'),

       ('Robert Kiyosaki', false, 'ENGLISH', 'Rich Dad Poor Dad', 'GIFT', 2017, 'book27.jpeg', 'leonie'),


       ('Hannah Luis', true, 'GERMAN', 'Bretonischer Zitronenzauber', 'GIFT', 2021, 'book28.jpeg', 'michiel'),
       ('Theodor Wolff', true, 'GERMAN', 'Theodor Wolff: Die Schwimmerin', 'EXCHANGE_FOR_CAKE', 2022, 'book29.jpeg',
        'christiaan'),


       ('Antoine de Saint-Exupéry', true, 'FRENCH', 'Le petit prince', 'GIFT', 1999, 'book30.jpeg', 'leonie'),
       ('Eugène Zamiatine', true, 'FRENCH', 'Nous autres', 'GIFT', 2016, 'book31.jpeg', 'leonie');

INSERT INTO user_book (user_username, book_id)
VALUES ('michiel', 11),
       ('michiel', 16),
       ('michiel', 24),
       ('christiaan', 6),
       ('christiaan', 7),
       ('christiaan', 18),
       ('christiaan', 28),
       ('leonie', 1),
       ('leonie', 7),
       ('leonie', 9),
       ('leonie', 22),
       ('leonie', 29);

INSERT INTO transactions (date, transaction_status, transaction_type, requester_username, requested_book_id,
                          provider_username, exchange_book_id)
VALUES ('2010-05-30 22:15:52', 'INITIALIZED', 'EXCHANGE_FOR_BOOK', 'michiel', 10, 'christiaan', null),
       ('2010-06-02 20:10:11', 'EXCHANGE_BOOK_SELECTED', 'EXCHANGE_FOR_BOOK', 'christiaan', 3, 'michiel', 13),
       ('2010-06-02 20:10:11', 'INITIALIZED', 'GIFT', 'michiel', 27, 'leonie', null),
       ('2010-06-02 20:10:11', 'EXCHANGE_BOOK_SELECTED', 'EXCHANGE_FOR_BOOK', 'leonie', 1, 'michiel', 20),
       ('2010-06-02 20:10:11', 'FINALIZED', 'EXCHANGE_FOR_CAKE', 'leonie', 14, 'christiaan', null);

INSERT INTO authorities (authority, username)
VALUES ('ROLE_USER', 'michiel'),
       ('ROLE_ADMIN', 'michiel'),
       ('ROLE_USER', 'christiaan'),
       ('ROLE_USER', 'leonie');