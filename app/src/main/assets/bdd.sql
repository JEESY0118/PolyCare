-- --------------------------------------------------------
--
-- Structure de la table `categories`
--

DROP TABLE IF EXISTS categories cascade ;
CREATE TABLE categories(
  category_id serial,
  category_name varchar(30) NOT NULL,
  PRIMARY KEY (category_id)
) ;
-- --------------------------------------------------------


-- --------------------------------------------------------
--
-- Structure de l'enum states
--
DROP TYPE IF EXISTS states cascade;
CREATE TYPE states AS ENUM ('A faire','En cours','Résolu');

-- --------------------------------------------------------



-- --------------------------------------------------------
--
-- Structure de l'enum priority
--
DROP TYPE IF EXISTS importances cascade;
CREATE TYPE importances AS ENUM ('Faible', 'Moyenne', 'Elevée');

-- --------------------------------------------------------



-- --------------------------------------------------------
--
-- Structure de la table `users`
--

DROP TABLE IF EXISTS users cascade ;
CREATE TABLE users (
  user_id serial ,
  user_firstname varchar(30) NOT NULL,
  user_lastname varchar(30) NOT NULL,
  user_phone varchar(20),
  PRIMARY KEY (user_id)
) ;

-- --------------------------------------------------------



-- --------------------------------------------------------
-- Structure de la table `events`
--

DROP TABLE IF EXISTS events cascade ;
CREATE TABLE events (
  event_id serial ,
  event_title varchar(30) NOT NULL,
  event_category int NOT NULL references categories,
  event_description varchar(500) DEFAULT NULL,
  event_reporter int references users,
  event_importance importances NOT NULL,
  event_state states NOT NULL,
  event_report_date date NOT NULL,
  PRIMARY KEY (event_id)
) ;
-- --------------------------------------------------------


INSERT INTO categories VALUES
    (0, 'Matérial');

INSERT INTO users VALUES
    (0, 'Wei', 'WANG', '1234567');

INSERT INTO users VALUES
    (1, 'Théo', 'Bonnet','2345678');

INSERT INTO users VALUES
    (2, 'Yon', 'Kooijman','3456789');

INSERT INTO users VALUES
    (3, 'Ali', 'Papa','4567890');

INSERT INTO events VALUES
    (0,'Manque du papier', 0, 'Il y a pas de papier dans imprimante', 0, 'Moyenne', 'A faire', '2018-04-09');
