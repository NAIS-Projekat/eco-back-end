-- Users
INSERT INTO users.users (email, enabled, name, surname, last_password_reset_date, password, phone_number, username, role, active, date_of_birth, gender)
VALUES
    ('peraperic@gmail.com', true, 'Pera', 'Perić', null, '$2a$10$siTymq2.Fp0J/K6vnBernO/vOXAHUXa4BCRFp14yoJvJbvE8qckGW', '+38763245234', 'peraperic', 'ADMINISTRATOR', true, '1990-01-01', 'MALE'),
    ('markoni@gmail.com', true, 'Marko', 'Milošević', null, '$2a$10$siTymq2.Fp0J/K6vnBernO/vOXAHUXa4BCRFp14yoJvJbvE8qckGW', '+38763245321', 'markom', 'REGISTERED_USER', true, '1985-05-15', 'MALE'),
    ('tati@gmail.com', true, 'Tatjana', 'Ljubičić', null, '$2a$10$siTymq2.Fp0J/K6vnBernO/vOXAHUXa4BCRFp14yoJvJbvE8qckGW', '+38763245987', 'ljubi.tatjana', 'REGISTERED_USER', true, '1978-11-25', 'FEMALE'),
    ('nikolagoric@gmail.com', true, 'Nikola', 'Gorić', null, '$2a$10$siTymq2.Fp0J/K6vnBernO/vOXAHUXa4BCRFp14yoJvJbvE8qckGW', '+38763245876', 'nikola.g', 'REGISTERED_USER', true, '1995-09-10', 'MALE'),
    ('jelastef@gmail.com', true, 'Jelena', 'Stefanović', null, '$2a$10$siTymq2.Fp0J/K6vnBernO/vOXAHUXa4BCRFp14yoJvJbvE8qckGW', '+38763245432', 'ac1', 'ACCOUNTANT', true, '1980-03-20', 'FEMALE'),
    ('helena.jankovic@gmail.com', true, 'Helena', 'Janković', null, '$2a$10$siTymq2.Fp0J/K6vnBernO/vOXAHUXa4BCRFp14yoJvJbvE8qckGW', '+381651231234', 'ac2', 'ACCOUNTANT', true, '1987-03-28', 'FEMALE'),
    ('goranniko@gmail.com', false, 'Goran', 'Nikolić', null, '$2a$10$siTymq2.Fp0J/K6vnBernO/vOXAHUXa4BCRFp14yoJvJbvE8qckGW', '+38763245678', 'niko.goran', 'BOARD_MEMBER', false, '1975-07-12', 'MALE'),
    ('markovicmil@gmail.com', true, 'Miloš', 'Marković', null, '$2a$10$siTymq2.Fp0J/K6vnBernO/vOXAHUXa4BCRFp14yoJvJbvE8qckGW', '+38763245123', 'milosm', 'BOARD_MEMBER', true, '1989-12-30', 'MALE'),
    ('anamatic@gmail.com', true, 'Ana', 'Matić', null, '$2a$10$siTymq2.Fp0J/K6vnBernO/vOXAHUXa4BCRFp14yoJvJbvE8qckGW', '+38763245234', 'ana.m', 'BOARD_MEMBER', true, '1977-04-05', 'FEMALE'),
    ('anastasijapetrovic@gmail.com', true, 'Anastasija', 'Petrović', null, '$2a$10$siTymq2.Fp0J/K6vnBernO/vOXAHUXa4BCRFp14yoJvJbvE8qckGW', '+38763245876', 'anapetrovic', 'PROJECT_MANAGER', true, '1992-08-18', 'FEMALE'),
    ('stevamil@gmail.com', true, 'Stevan', 'Milićić', null, '$2a$10$siTymq2.Fp0J/K6vnBernO/vOXAHUXa4BCRFp14yoJvJbvE8qckGW', '+38763245321', 'stevaj', 'PROJECT_MANAGER', true, '1983-06-25', 'MALE'),
    ('stefanstefanovic@gmail.com', true, 'Stefan', 'Belić', null, '$2a$10$siTymq2.Fp0J/K6vnBernO/vOXAHUXa4BCRFp14yoJvJbvE8qckGW', '+38763245987', 'stefans', 'PROJECT_COORDINATOR', true, '1987-10-15', 'MALE'),
    ('micaj@gmail.com', true, 'Milica', 'Jović', null, '$2a$10$siTymq2.Fp0J/K6vnBernO/vOXAHUXa4BCRFp14yoJvJbvE8qckGW', '+38763245432', 'mica', 'PROJECT_COORDINATOR', true, '1990-02-28', 'FEMALE'),
    ('marijamarijanovic@gmail.com', false, 'Marija', 'Marijanović', null, '$2a$10$siTymq2.Fp0J/K6vnBernO/vOXAHUXa4BCRFp14yoJvJbvE8qckGW', '+38763245678', 'marija.m', 'EDUCATOR', true, '1986-09-07', 'FEMALE'),
    ('ljubabuba@gmail.com', true, 'Ljubica', 'Atić', null, '$2a$10$siTymq2.Fp0J/K6vnBernO/vOXAHUXa4BCRFp14yoJvJbvE8qckGW', '+38763245123', 'ljubica.a', 'EDUCATOR', true, '1994-03-12', 'FEMALE');

-- Education
INSERT INTO education.lecture_category (description)
VALUES
    ('Forest Ecosystems'),
    ('Marine Biology'),
    ('Desert Wildlife'),
    ('Arctic Exploration'),
    ('Rainforest Ecology');

INSERT INTO education.lecture (difficulty, max_recommended_age, min_recommended_age, creator_id, content, name)
VALUES
    (1, 12, 8, 15, E'# Introduction to Forest Ecosystems\n' ||
                   E'This lecture provides an overview of forest ecosystems, covering various aspects such as biodiversity, ecological interactions, and human impact on forests.\n' ||
                   E'Forests are vital for the health of our planet, serving as habitats for countless species and playing a crucial role in carbon sequestration.\n' ||
                   E'In this session, we will explore the different layers of a forest, from the forest floor to the emergent layer, and discuss the unique characteristics of each.\n' ||
                   E'Additionally, we will delve into the various types of forests found around the world, including tropical rainforests, temperate forests, and boreal forests.\n' ||
                   E'Throughout the lecture, we will examine the complex relationships between plants, animals, and microorganisms that make up forest ecosystems.\n' ||
                   E'Topics such as nutrient cycling, energy flow, and ecological succession will be explored to provide a comprehensive understanding of forest dynamics.\n' ||
                   E'Furthermore, we will address the threats facing forests today, such as deforestation, habitat fragmentation, and climate change, and discuss strategies for conservation and sustainable management.\n' ||
                   E'By the end of this lecture, participants will gain a deeper appreciation for the importance of forests and the urgent need to protect them for future generations.\n',
     'Forest 101'),
    (2, 14, 10, 15, E'# Exploring Marine Habitats\n' ||
                    E'Marine habitats encompass a vast and diverse array of ecosystems, ranging from coral reefs and kelp forests to deep-sea trenches and hydrothermal vents.\n' ||
                    E'In this lecture, we will embark on a journey beneath the waves to discover the fascinating world of marine life.\n' ||
                    E'We will begin by examining the unique physical and chemical properties of the ocean that shape marine habitats and influence the distribution of species.\n' ||
                    E'From there, we will explore the incredible diversity of marine organisms, from microscopic plankton to majestic whales, and everything in between.\n' ||
                    E'Participants will learn about the different zones of the ocean, including the sunlit surface waters, the twilight zone, and the dark depths of the abyss.\n' ||
                    E'Throughout the lecture, we will discuss the various adaptations that enable marine organisms to thrive in their respective environments, from camouflage and bioluminescence to hydrodynamic shapes and specialized organs.\n' ||
                    E'We will also examine the complex ecological relationships that exist within marine ecosystems, such as predator-prey interactions, symbiotic partnerships, and competitive exclusion.\n' ||
                    E'Furthermore, we will address the human impacts on marine habitats, including pollution, overfishing, and climate change, and explore potential solutions to mitigate these threats.\n' ||
                    E'By the end of this lecture, participants will develop a deeper understanding of the importance of marine habitats and the urgent need to conserve and protect them for future generations.\n',
     'Marine Life Overview'),
    (0, 10, 6, 15, E'# Adaptations of Desert Animals\n' ||
                   E'Deserts are harsh and unforgiving environments characterized by extreme temperatures, sparse vegetation, and limited water resources.\n' ||
                   E'Despite these challenges, desert animals have evolved a remarkable array of adaptations to survive in this hostile habitat.\n' ||
                   E'In this lecture, we will explore the fascinating world of desert wildlife and uncover the secrets of their survival.\n' ||
                   E'We will begin by discussing the physical adaptations of desert animals, such as camels'' humps for water storage, kangaroos'' efficient kidneys for water conservation, and fennec foxes'' large ears for heat regulation.\n' ||
                   E'Participants will learn about the behavioral adaptations employed by desert animals to cope with the scarcity of resources, including nocturnal activity to avoid daytime heat, burrowing to escape temperature extremes, and communal living for protection.\n' ||
                   E'Furthermore, we will examine the ecological roles played by desert species in maintaining ecosystem balance, such as pollination, seed dispersal, and predator-prey dynamics.\n' ||
                   E'Throughout the lecture, we will highlight some of the iconic desert animals, including the Arabian camel, the desert tortoise, the meerkat, and the sidewinder rattlesnake.\n' ||
                   E'We will also discuss the conservation challenges facing desert ecosystems, including habitat destruction, climate change, and human encroachment, and explore potential conservation strategies.\n' ||
                   E'By the end of this lecture, participants will gain a newfound appreciation for the resilience and adaptability of desert animals and the importance of conserving their fragile habitats.\n',
        'Desert Survival'),
    (1, 16, 12, 15, E'# Life in the Arctic\n' ||
                    E'The Arctic is a vast and remote region located at the northernmost part of Earth, characterized by icy landscapes, extreme cold, and unique wildlife.\n' ||
                    E'In this lecture, we will embark on an expedition to explore the wonders of the Arctic and learn about the remarkable adaptations of its inhabitants.\n' ||
                    E'We will begin by examining the physical geography of the Arctic, including its ice caps, glaciers, and permafrost, and discuss the role of the Arctic in regulating global climate patterns.\n' ||
                    E'Participants will discover the rich biodiversity of the Arctic, from polar bears and Arctic foxes to seals, whales, and migratory birds.\n' ||
                    E'We will delve into the unique adaptations of Arctic animals, such as thick fur and blubber for insulation, specialized paws for walking on ice, and hibernation strategies to survive the long winter months.\n' ||
                    E'Furthermore, we will explore the cultural heritage of the indigenous peoples of the Arctic, including their traditional knowledge, art, and ways of life.\n' ||
                    E'Throughout the lecture, we will address the environmental challenges facing the Arctic, including melting sea ice, habitat loss, and pollution, and discuss efforts to protect this fragile ecosystem.\n' ||
                    E'By the end of this lecture, participants will develop a deeper understanding of the Arctic ecosystem and the urgent need to conserve its biodiversity and cultural heritage for future generations.\n',
     'Arctic Adventures'),
    (0, 8, 4, 15, E'# Discovering Rainforest Biodiversity\n' ||
                  E'Rainforests are some of the most biodiverse ecosystems on Earth, teeming with life and exhibiting a remarkable array of plants, animals, and microorganisms.\n' ||
                  E'In this lecture, we will venture into the heart of the rainforest to uncover its hidden treasures and explore the wonders of its biodiversity.\n' ||
                  E'We will begin by discussing the structure and layers of the rainforest, from the emergent layer to the forest floor, and examine the unique adaptations of plants and animals to life in this dense environment.\n' ||
                  E'Participants will learn about the intricate web of ecological relationships that exist within the rainforest, including symbiotic interactions, predator-prey dynamics, and nutrient cycling.\n' ||
                  E'We will highlight some of the iconic species found in the rainforest, such as jaguars, toucans, orchids, and giant ceiba trees, and discuss their ecological importance.\n' ||
                  E'Furthermore, we will explore the cultural significance of rainforests to indigenous peoples, including their traditional knowledge, spiritual beliefs, and sustainable practices.\n' ||
                  E'Throughout the lecture, we will address the threats facing rainforest ecosystems, including deforestation, habitat fragmentation, and climate change, and discuss strategies for conservation and sustainable management.\n' ||
                  E'By the end of this lecture, participants will develop a deeper appreciation for the beauty and complexity of rainforest biodiversity and the urgent need to protect these vital ecosystems.\n',
     'Rainforest Wonders');

INSERT INTO education.has_category (category_id, lecture_id)
VALUES
    (1, 1), -- Forest 101 belongs to Forest Ecosystems category
    (2, 2), -- Marine Life Overview belongs to Marine Biology category
    (3, 3), -- Desert Survival belongs to Desert Wildlife category
    (4, 4), -- Arctic Adventures belongs to Arctic Exploration category
    (5, 5), -- Rainforest Wonders belongs to Rainforest Ecology category
    (1, 3), -- Desert Survival also belongs to Forest Ecosystems category
    (3, 1); -- Forest 101 also belongs to Desert Wildlife category

-- Questions and answers for Lecture: Introduction to Forest Ecosystems
INSERT INTO education.question (order_in_lecture, type, lecture_id, content)
VALUES
    -- Questions for "Introduction to Forest Ecosystems" lecture
    (1, 0, 1, 'What are some key characteristics of forest ecosystems?'),
    (2, 1, 1, 'True or false: Forests play a minor role in carbon sequestration?'),
    (3, 2, 1, 'What is the primary threat to forests today?');

INSERT INTO education.answer (is_correct, question_id, content)
VALUES
    -- Answers for the first question
    (TRUE, 1, 'High biodiversity, carbon sequestration, ecological interactions'),
    (FALSE, 1, 'Low biodiversity, carbon emissions, ecological isolation'),
    (FALSE, 1, 'High pollution, low biodiversity, extreme temperatures'),

    -- Answers for the second question
    (FALSE, 2, 'True'),
    (TRUE, 2, 'False'),

    -- Answers for the third question
    (TRUE, 3, 'Deforestation'),
    (FALSE, 3, 'Ocean pollution'),
    (FALSE, 3, 'Desertification');

-- Questions and answers for Lecture: Exploring Marine Habitats
INSERT INTO education.question (order_in_lecture, type, lecture_id, content)
VALUES
    -- Questions for "Exploring Marine Habitats" lecture
    (1, 0, 2, 'What are some examples of marine habitats?'),
    (2, 1, 2, 'Which zone of the ocean receives the least amount of sunlight?'),
    (3, 2, 2, 'What are some human impacts on marine habitats?');

INSERT INTO education.answer (is_correct, question_id, content)
VALUES
    -- Answers for the first question
    (TRUE, 4, 'Coral reefs, kelp forests, deep-sea trenches'),
    (FALSE, 4, 'Tundra, rainforests, deserts'),
    (FALSE, 4, 'Mountains, lakes, rivers'),

    -- Answers for the second question
    (TRUE, 5, 'Abyssal zone'),
    (FALSE, 5, 'Sunlit zone'),
    (FALSE, 5, 'Twilight zone'),

    -- Answers for the third question
    (TRUE, 6, 'Pollution'),
    (TRUE, 6, 'Overfishing'),
    (TRUE, 6, 'Climate change');

-- Questions and answers for Lecture: Adaptations of Desert Animals
INSERT INTO education.question (order_in_lecture, type, lecture_id, content)
VALUES
    -- Questions for "Adaptations of Desert Animals" lecture
    (1, 0, 3, 'What are some physical adaptations of desert animals?'),
    (2, 1, 3, 'True or false: Desert animals are primarily active during the day?'),
    (3, 2, 3, 'What conservation challenges do desert ecosystems face?');

INSERT INTO education.answer (is_correct, question_id, content)
VALUES
    -- Answers for the first question
    (TRUE, 7, 'Water storage, efficient kidneys, large ears'),
    (FALSE, 7, 'Gills, fins, scales'),
    (FALSE, 7, 'Wings, feathers, beaks'),

    -- Answers for the second question
    (FALSE, 8, 'True'),
    (TRUE, 8, 'False'),

    -- Answers for the third question
    (TRUE, 9, 'Habitat destruction'),
    (TRUE, 9, 'Climate change'),
    (TRUE, 9, 'Human encroachment');

-- Questions and answers for Lecture: Life in the Arctic
INSERT INTO education.question (order_in_lecture, type, lecture_id, content)
VALUES
    -- Questions for "Life in the Arctic" lecture
    (1, 0, 4, 'What are some characteristics of the Arctic ecosystem?'),
    (2, 1, 4, 'True or false: Arctic animals do not need to adapt to extreme cold?'),
    (3, 2, 4, 'What is a significant threat to the Arctic ecosystem?');

INSERT INTO education.answer (is_correct, question_id, content)
VALUES
    -- Answers for the first question
    (TRUE, 10, 'Extreme cold, ice caps, polar bears'),
    (FALSE, 10, 'Deserts, heat, camels'),
    (FALSE, 10, 'Tropical climate, rainforests, jaguars'),

    -- Answers for the second question
    (FALSE, 11, 'True'),
    (TRUE, 11, 'False'),

    -- Answers for the third question
    (TRUE, 12, 'Melting sea ice'),
    (FALSE, 12, 'Desertification'),
    (FALSE, 12, 'Volcanic eruptions');

-- Questions and answers for Lecture: Discovering Rainforest Biodiversity
INSERT INTO education.question (order_in_lecture, type, lecture_id, content)
VALUES
    -- Questions for "Discovering Rainforest Biodiversity" lecture
    (1, 0, 5, 'What makes rainforests one of the most biodiverse ecosystems?'),
    (2, 1, 5, 'True or false: Rainforests have low ecological importance?'),
    (3, 2, 5, 'What are some conservation challenges facing rainforests?');

INSERT INTO education.answer (is_correct, question_id, content)
VALUES
    -- Answers for the first question
    (TRUE, 13, 'High number of species, dense vegetation, complex interactions'),
    (FALSE, 13, 'Low number of species, sparse vegetation, simple interactions'),
    (FALSE, 13, 'Moderate number of species, moderate vegetation, moderate interactions'),

    -- Answers for the second question
    (FALSE, 14, 'True'),
    (TRUE, 14, 'False'),

    -- Answers for the third question
    (TRUE, 15, 'Deforestation'),
    (TRUE, 15, 'Habitat fragmentation'),
    (TRUE, 15, 'Climate change');

INSERT INTO education.test_execution (finished, points, lecture_id, user_id)
VALUES
    (TRUE, 25, 1, 2),
    (TRUE, 15, 1, 3),
    (TRUE, 5, 1, 4),
    (TRUE, 23, 2, 2),
    (TRUE, 11, 2, 3),
    (FALSE, 0, 2, 4),
    (TRUE, 7, 3, 2);


-- Finance
INSERT INTO users.accountants (id, wage, working_hours, overtime_wage)
VALUES (5, 18.7, 7, 20),
       (6, 18.7, 7, 20);
INSERT INTO users.registered_users (id, points, registration_date)
VALUES (2, 100, '2024-05-28 12:00:00'),
       (3, 150, '2024-05-29 10:00:00'),
       (4, 200, '2024-05-30 08:00:00');
INSERT INTO users.organization_members (id, wage, working_hours, overtime_wage)
VALUES
    (5, 18.7, 7, 20),
    (6, 18.7, 7, 20),
    (7, 19, 7, 20.5),
    (8, 19, 7, 20.5),
    (9, 19, 7, 20.5),
    (10, 18.7, 8, 20),
    (11, 18.7, 8, 20),
    (12, 18.7, 8, 20),
    (13, 18.7, 8, 20),
    (14, 18.7, 6, 21),
    (15, 18.7, 6, 21.2);

INSERT INTO finance.fixed_expenses (type, expense_type,employee, overtime_hours, start_date, end_date, amount, creator, created_on, description)
VALUES
    -- FixedExpenses - January 2024
    ('SALARY', 'SALARY',  5, 0, '2024-01-01', '2024-02-01', 2350, 5, '2024-02-12 12:00:00.000', ''),
    ('SALARY', 'SALARY',  6, 0, '2024-01-01', '2024-02-01', 2350, 5, '2024-02-12 12:00:00.000', ''),
    ('SALARY', 'SALARY',  7, 0, '2024-01-01', '2024-02-01', 2350, 5, '2024-02-12 12:00:00.000', ''),
    ('SALARY', 'SALARY',  8, 0, '2024-01-01', '2024-02-01', 2350, 5, '2024-02-12 12:00:00.000', ''),
    ('SALARY', 'SALARY', 10, 0, '2024-01-01', '2024-02-01', 2350, 5, '2024-02-12 12:00:00.000', ''),
    ('SALARY', 'SALARY', 11, 0, '2024-01-01', '2024-02-01', 2350, 5, '2024-02-12 12:00:00.000', ''),
    ('SALARY', 'SALARY', 12, 0, '2024-01-01', '2024-02-01', 2350, 5, '2024-02-12 12:00:00.000', ''),
    ('SALARY', 'SALARY', 13, 0, '2024-01-01', '2024-02-01', 2350, 5, '2024-02-12 12:00:00.000', ''),
    ('SALARY', 'SALARY', 14, 0, '2024-01-01', '2024-02-01', 2350, 5, '2024-02-12 12:00:00.000', ''),
    ('SALARY', 'SALARY', 15, 0, '2024-01-01', '2024-02-01', 2350, 5, '2024-02-12 12:00:00.000', ''),
    ('RENT', 'FixedExpenses', null, null, '2024-01-01', '2024-02-01', 5000, 5, '2024-02-12 12:00:00.000', 'Rent for January 2024');

INSERT INTO finance.budget_plan (status,  start_date, end_date, name, description, author, last_updated_on_date)
VALUES
    ('DRAFT', '2024-01-01', '2024-12-31', '2024 Annual Budget', 'Annual Budget Plan', 5, '2024-04-04'),
    ('DRAFT', '2025-01-01', '2025-12-31', '2025 Annual Budget', 'Annual Budget Plan', 5, '2024-04-04'),
    ('PENDING', '2024-04-01', '2024-06-30', 'Q2 2024 Budget', 'Quarterly Budget Plan', 5, '2024-04-04'),
    ('APPROVED', '2024-01-01', '2024-12-31', '2024 Annual Budget (Approved)', 'Approved Annual Budget Plan', 5, '2024-04-04'),
    ('REJECTED', '2024-01-01', '2024-12-31', '2024 Annual Budget (Rejected)', 'Rejected Annual Budget Plan', 5, '2024-04-04'),
    ('CLOSED', '2024-01-01', '2024-12-31', '2024 Annual Budget (Closed)', 'Closed Annual Budget Plan', 5, '2024-04-04'),
    ('DRAFT', '2026-01-01', '2026-12-31', '2026 Annual Budget', 'Annual Budget Plan', 5, '2024-04-04'),
    ('DRAFT', '2027-01-01', '2027-12-31', '2027 Annual Budget', 'Annual Budget Plan', 6, '2024-04-04'),
    ('PENDING', '2024-07-01', '2024-09-30', 'Q3 2024 Budget', 'Quarterly Budget Plan', 5, '2024-04-04'),
    ('APPROVED', '2025-01-01', '2025-12-31', '2025 Annual Budget (Approved)', 'Approved Annual Budget Plan', 6, '2024-04-04'),
    ('REJECTED', '2024-07-01', '2024-12-31', '2024 Annual Budget (Rejected)', 'Rejected Annual Budget Plan', 6, '2024-04-04');

-- INSERT INTO finance.fixed_expenses_estimation
--     (id, budget_plan, fixed_expense_id, type, employee, overtime_hours, start_date, end_date, amount, creator, created_on, description)
-- VALUES
--     ( 1, 1, 0,   'RENT', null, 0,'2024-01-01', '2024-02-01', 3500, 5, '2024-02-12 12:00:00.000', 'Rent for January 2024'),
--     ( 2, 1, 0, 'SALARY',    5, 0,'2024-01-01', '2024-02-01', 2350, 5, '2024-02-12 12:00:00.000', ''),
--     ( 3, 1, 0, 'SALARY',    6, 0,'2024-01-01', '2024-02-01', 2350, 5, '2024-02-12 12:00:00.000', ''),
--     ( 4, 1, 0, 'SALARY',    7, 0,'2024-01-01', '2024-02-01', 2350, 5, '2024-02-12 12:00:00.000', ''),
--     ( 5, 1, 0, 'SALARY',    8, 0,'2024-01-01', '2024-02-01', 2350, 5, '2024-02-12 12:00:00.000', ''),
--     ( 6, 1, 0, 'SALARY',    9, 0,'2024-01-01', '2024-02-01', 2350, 5, '2024-02-12 12:00:00.000', ''),
--     ( 7, 1, 0, 'SALARY',   10, 0,'2024-01-01', '2024-02-01', 2350, 5, '2024-02-12 12:00:00.000', ''),
--     ( 8, 1, 0, 'SALARY',   11, 0,'2024-01-01', '2024-02-01', 2350, 5, '2024-02-12 12:00:00.000', ''),
--     ( 9, 1, 0, 'SALARY',   12, 0,'2024-01-01', '2024-02-01', 2350, 5, '2024-02-12 12:00:00.000', ''),
--     (10, 1, 0, 'SALARY',   13, 0,'2024-01-01', '2024-02-01', 2350, 5, '2024-02-12 12:00:00.000', ''),
--     (11, 1, 0, 'SALARY',   14, 0,'2024-01-01', '2024-02-01', 2350, 5, '2024-02-12 12:00:00.000', ''),
--     (12, 1, 0, 'SALARY',   15, 0,'2024-01-01', '2024-02-01', 2350, 5, '2024-02-12 12:00:00.000', '');

INSERT INTO finance.organization_goal (
    creator, status, title, start_date, end_date, description, rationale, priority)
VALUES
    (9, 'VALID', 'green energy', '2024-01-01', null, 'Focuses on promoting the adoption and utilization of renewable energy sources such as solar, wind, and hydroelectric power to reduce carbon emissions. ', 'By investing in green energy initiatives, the organization aims to transition towards a sustainable and environmentally friendly energy infrastructure.', 1),
    (8, 'VALID', 'green knowledge', '2024-01-01', null, 'Aims to educate and raise awareness about environmental conservation, sustainability practices, and the importance of preserving natural resources. ', 'Organization seeks to empower individuals and communities with the knowledge and skills needed to contribute to a greener and healthier planet.', 1),
    (8, 'VALID', 'clean air', '2024-01-01', null, 'This goal is dedicated to combating air pollution and improving air quality in urban and industrial areas. ', 'Organization aims to ensure that everyone has access to clean and breathable air, safeguarding public health and the environment.', 1),
    (8, 'VALID', 'green city', '2024-01-01', null, 'Envisions transforming urban spaces into sustainable and eco-friendly environments that prioritize green infrastructure', 'Organization seeks to promote sustainable urban development practices that enhance quality of life, biodiversity, and resilience to climate change.', 1),
    (9, 'VALID', 'nature', '2024-01-01', null, 'Focuses on preserving and protecting natural ecosystems, biodiversity hotspots, and endangered species. ', 'Organization aims to safeguard natural habitats and biodiversity, ensuring that future generations can continue to benefit from the beauty and resources that nature provides.', 1),
    (9, 'ARCHIVED', 'renewable energy expansion', '2023-01-01', '2024-01-01', 'Aims to accelerate the deployment and adoption of renewable energy technologies such as solar, wind, and biomass', 'The organization strives to lead the transition to a sustainable energy future by investing in renewable energy infrastructure and fostering innovation in clean energy technologies.', 1),
    (8, 'ARCHIVED', 'sustainable agriculture promotion', '2023-01-01', '2024-01-01', 'Focuses on promoting sustainable farming practices such as organic farming, agroforestry, and permaculture to enhance food security, protect ecosystems, and mitigate climate change.', 'Organization aims to empower farmers with sustainable agricultural techniques and promote the adoption of practices that restore soil health, conserve water, and preserve biodiversity.', 1),
    (8, 'ARCHIVED', 'urban greening initiative', '2023-01-01', '2024-01-01', 'Aims to enhance urban biodiversity, improve air quality, and mitigate urban heat island effects by increasing green spaces, planting trees, and implementing green infrastructure in cities.', 'Organization seeks to create healthier and more livable cities by promoting urban greening initiatives that enhance environmental sustainability, public health, and community well-being.', 1),
    (8, 'ARCHIVED', 'marine conservation program', '2023-01-01', '2024-01-01', 'Focuses on protecting and restoring marine ecosystems, conserving marine biodiversity, and promoting sustainable fisheries management to ensure the long-term health and resilience of oceans.', 'Organization aims to safeguard marine biodiversity, support sustainable fisheries, and mitigate the impacts of climate change on ocean ecosystems through science-based conservation efforts and community engagement.', 1),
    (9, 'ARCHIVED', 'environmental education outreach', '2023-01-01', '2024-01-01', 'Aims to raise awareness about environmental issues, inspire action for conservation, and promote sustainability through education, outreach programs, and public engagement initiatives.', 'Organization seeks to empower individuals and communities with knowledge and skills to address environmental challenges and foster a culture of environmental stewardship and sustainability.', 1),
    (9, 'ARCHIVED', 'renewable energy advocacy', '2022-01-01', '2023-01-01', 'Aims to advocate for policies and incentives that promote the adoption of renewable energy sources and accelerate the transition to a low-carbon economy.', 'By advocating for renewable energy policies and initiatives, the organization aims to drive systemic change and create a supportive environment for clean energy investment and innovation.', 1),
    (8, 'ARCHIVED', 'sustainability workshops', '2022-01-01', '2023-01-01', 'Focuses on organizing workshops, seminars, and training sessions to educate individuals and organizations about sustainability practices, resource conservation, and environmental stewardship.', 'Organization aims to empower participants with practical knowledge and skills to integrate sustainability principles into their personal and professional lives, fostering a culture of sustainability and responsible citizenship.', 1),
    (8, 'ARCHIVED', 'air quality monitoring program', '2022-01-01', '2023-01-01', 'Aims to establish a comprehensive air quality monitoring program to track air pollution levels, identify sources of pollution, and inform policy decisions and public health interventions.', 'Organization seeks to improve air quality, protect public health, and raise awareness about the impacts of air pollution on human health and the environment through data-driven analysis and targeted interventions.', 1),
    (8, 'ARCHIVED', 'green infrastructure development', '2022-01-01', '2023-01-01', 'Focuses on planning, designing, and implementing green infrastructure projects such as urban parks, green roofs, and permeable pavement to enhance ecosystem services, mitigate urban heat island effects, and promote climate resilience.', 'Organization aims to create sustainable and resilient communities by integrating green infrastructure into urban planning and development processes, enhancing quality of life and environmental sustainability.', 1);

-- Projects
INSERT INTO projects.projects (name, description, duration_months, budget, type, status, manager_id)
VALUES
    ('Conservation of Wetlands', 'Protecting and restoring wetland ecosystems', 24, 50000.00, 'INTERNAL', 'DRAFT', 9),
    ('Reforestation Initiative', 'Planting trees and restoring forest habitats', 18, 30000.00, 'EXTERNAL', 'PENDING', 9),
    ('Urban Green Spaces Development', 'Creating parks and green areas in urban settings', 12, 25000.00, 'INTERNAL', 'APPROVED', 9),
    ('River Cleanup Campaign', 'Cleaning up polluted rivers and waterways', 6, 10000.00, 'EXTERNAL', 'ONGOING', 9),
    ('Biodiversity Monitoring Program', 'Monitoring and preserving local biodiversity', 36, 75000.00, 'INTERNAL', 'REJECTED', 9),
    ('Sustainable Agriculture Project', 'Promoting eco-friendly farming practices', 24, 60000.00, 'EXTERNAL', 'ARCHIVED', 9),
    ('Ocean Conservation Initiative', 'Protecting marine life and habitats', 36, 80000.00, 'INTERNAL', 'ONGOING', 9),
    ('Eco-friendly Transportation Campaign', 'Promoting sustainable transport options', 12, 20000.00, 'EXTERNAL', 'PENDING', 9),
    ('Wildlife Habitat Restoration', 'Restoring habitats for endangered species', 24, 60000.00, 'INTERNAL', 'ONGOING', 10),
    ('Green Energy Development Project', 'Promoting renewable energy solutions', 18, 35000.00, 'EXTERNAL', 'APPROVED', 10),
    ('Community Gardens Initiative', 'Creating communal spaces for sustainable gardening', 9, 15000.00, 'INTERNAL', 'ONGOING', 10),
    ('Eco-tourism Promotion Program', 'Encouraging responsible tourism practices', 24, 40000.00, 'EXTERNAL', 'PENDING', 10),
    ('Waste Recycling Campaign', 'Promoting waste reduction and recycling', 6, 10000.00, 'INTERNAL', 'ARCHIVED', 13),
    ('Green Buildings Certification Project', 'Promoting eco-friendly building standards', 36, 90000.00, 'EXTERNAL', 'APPROVED', 10),
    ('Climate Change Adaptation Strategy', 'Developing strategies for climate resilience', 24, 70000.00, 'INTERNAL', 'ONGOING', 10);

INSERT INTO projects.documents (document_id, project_id, name, progress, status)
VALUES
    (1, 1, 'Conservation Plan', 0.25, 'In Progress - less than halfway'), -- Conservation of Wetlands
    (1, 2, 'Reforestation Plan', 0.10, 'In Progress - less than halfway'), -- Reforestation Initiative
    (1, 3, 'Green Spaces Proposal', 0.50, 'Halfway Done'), -- Urban Green Spaces Development
    (1, 4, 'Cleanup Strategy', 0.75, 'In Progress - more than halfway'), -- River Cleanup Campaign
    (1, 5, 'Monitoring Protocol', 0.40, 'In Progress - less than halfway'), -- Biodiversity Monitoring Program
    (1, 6, 'Agriculture Plan', 0.60, 'In Progress - more than halfway'), -- Sustainable Agriculture Project
    (1, 7, 'Ocean Protection Plan', 0.80, 'In Progress - more than halfway'), -- Ocean Conservation Initiative
    (1, 8, 'Transportation Proposal', 0.20, 'In Progress - less than halfway'), -- Eco-friendly Transportation Campaign
    (1, 9, 'Habitat Restoration Plan', 0.70, 'In Progress - more than halfway'), -- Wildlife Habitat Restoration
    (1, 10, 'Energy Development Plan', 0.30, 'In Progress - less than halfway'), -- Green Energy Development Project
    (1, 11, 'Gardens Blueprint', 0.90, 'Completed'), -- Community Gardens Initiative
    (1, 12, 'Tourism Promotion Plan', 0.15, 'In Progress - less than halfway'), -- Eco-tourism Promotion Program
    (1, 13, 'Recycling Strategy', 1.00, 'Completed'), -- Waste Recycling Campaign
    (1, 14, 'Green Buildings Plan', 0.45, 'In Progress - less than halfway'), -- Green Buildings Certification Project
    (1, 15, 'Climate Adaptation Strategy', 0.55, 'In Progress - more than halfway'); -- Climate Change Adaptation Strategy

INSERT INTO projects.document_versions (version, document_id, project_id, file_path, author)
VALUES
    (1, 1, 1, '/path/to/document1.pdf', 9), -- Conservation of Wetlands
    (1, 2, 2, '/path/to/document2.pdf', 9), -- Reforestation Initiative
    (1, 3, 3, '/path/to/document3.pdf', 9),  -- Urban Green Spaces Development
    (1, 4, 4, '/path/to/document4.pdf', 9), -- River Cleanup Campaign
    (1, 5, 5, '/path/to/document5.pdf', 9), -- Biodiversity Monitoring Program
    (1, 6, 6, '/path/to/document6.pdf', 9), -- Sustainable Agriculture Project
    (1, 7, 7, '/path/to/document7.pdf', 9), -- Ocean Conservation Initiative
    (1, 8, 8, '/path/to/document8.pdf', 9), -- Eco-friendly Transportation Campaign
    (1, 9, 9, '/path/to/document9.pdf', 10), -- Wildlife Habitat Restoration
    (1, 10, 10, '/path/to/document10.pdf', 10), -- Green Energy Development Project
    (1, 11, 11, '/path/to/document11.pdf', 10), -- Community Gardens Initiative
    (1, 12, 12, '/path/to/document12.pdf', 10), -- Eco-tourism Promotion Program
    (1, 13, 13, '/path/to/document13.pdf', 10), -- Waste Recycling Campaign
    (1, 14, 14, '/path/to/document14.pdf', 10), -- Green Buildings Certification Project
    (1, 15, 15, '/path/to/document15.pdf', 10); -- Climate Change Adaptation Strategy

-- TODO add assignments, team members, reviews

--Volunteering
INSERT INTO volunteering.volunteer_actions (title, project_id, supervisor_id, creator_id, participant_count, application_limit, difficulty, description, date, duration, volunteer_action_type, volunteer_action_status, budget, project_dependency)
VALUES
    ('Wetland Cleanup', 1, 12, 13, 20, false, 'EASY', 'Clean up the wetland areas', '2024-06-01 09:00:00', 5, 'PLANTING', 'FINISHED', 2000.00, true),
    ('Tree Planting Event', 12, 13, 13, 50, true, 'MODERATE', 'Plant trees in deforested areas', '2024-05-20 10:00:00', 8, 'PLANTING', 'FINISHED', 5000.00, true),
    ('Park Renovation', 3, 13, 12, 30, true, 'MODERATE', 'Renovate local urban parks', '2024-06-25 09:00:00', 6, 'COMPETITION', 'UPCOMING', 3000.00, true),
    ('River Cleanup Drive', 4, 13, 12, 15, false, 'CHALLENGING', 'Clean up the local river', '2024-06-30 08:00:00', 7, 'PLANTING', 'PENDING', 1000.00, true),
    ('Biodiversity Survey', 5, 13, 12, 10, true, 'CHALLENGING', 'Survey local biodiversity', '2024-07-05 07:00:00', 10, 'PLANTING', 'REJECTED', 4000.00, true);

INSERT INTO volunteering.volunteer_action_registered_users (volunteer_action_id, user_id, appeared) VALUES
(2,2,true),
(2,3,false),
(2,4,true),
(3,2,false),
(3,3,false),
(3,4,false),
(4,2,false),
(1,2,true);

INSERT INTO projects.team_members (user_id, project_id) VALUES
(13, 1),
(13, 2),
(13, 3),
(13, 4),
(13, 12),
(12, 3);


--Notifications
INSERT INTO volunteering.notifications (volunteer_action_id, description, notification_type, date)
VALUES
    (2, 'Reminder: Tree Planting Event is scheduled for next week!', 'MODIFICATION', '2024-06-13 08:00:00'),
    (3, 'Park Renovation has been postponed to a later date.', 'DELETION', '2024-06-23 10:00:00'),
    (4, 'River Cleanup Drive has been cancelled due to weather conditions.', 'DELETION', '2024-06-29 15:00:00');

--User Notifications
INSERT INTO volunteering.user_notifications (user_id, notification_id, is_read)
VALUES
    (2, 1, false),
    (2, 2, false),
    (2, 3, false);
