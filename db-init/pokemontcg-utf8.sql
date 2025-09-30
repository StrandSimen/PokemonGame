--
-- PostgreSQL database dump
--

\restrict 4TKg6ok2nDaBk3vOyylWLq1YA15DZfc4cQljnvn8MXUz7PIVPMylC3JaUPSvdWY

-- Dumped from database version 17.6 (Debian 17.6-1.pgdg13+1)
-- Dumped by pg_dump version 17.6 (Debian 17.6-1.pgdg13+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: cards; Type: TABLE; Schema: public; Owner: admin
--

CREATE TABLE public.cards (
    pokedex_number integer NOT NULL,
    hp character varying(255),
    image_url character varying(500),
    name character varying(255) NOT NULL,
    types character varying(100)
);


ALTER TABLE public.cards OWNER TO admin;

--
-- Data for Name: cards; Type: TABLE DATA; Schema: public; Owner: admin
--

COPY public.cards (pokedex_number, hp, image_url, name, types) FROM stdin;
105	80	https://images.pokemontcg.io/ex11/10.png	Marowak ╬┤	Fighting,Metal
15	80	https://images.pokemontcg.io/ecard3/H4.png	Beedrill	Grass
22	70	https://images.pokemontcg.io/gym1/7.png	Lt. Surge's Fearow	Colorless
71	140	https://images.pokemontcg.io/sm7/3.png	Victreebel	Grass
87	80	https://images.pokemontcg.io/ex6/3.png	Dewgong	Water
12	300	https://images.pokemontcg.io/swsh3/2.png	Butterfree VMAX	Grass
130	70	https://images.pokemontcg.io/base5/8.png	Dark Gyarados	Water
145	100	https://images.pokemontcg.io/ru1/8.png	Zapdos	Lightning
11	80	https://images.pokemontcg.io/swsh2/2.png	Metapod	Grass
89	70	https://images.pokemontcg.io/ex12/11.png	Muk	Grass
39	50	https://images.pokemontcg.io/basep/7.png	Jigglypuff	Colorless
122	80	https://images.pokemontcg.io/det1/11.png	Mr. Mime	Psychic
73	60	https://images.pokemontcg.io/si1/10.png	Tentacruel	Water
51	60	https://images.pokemontcg.io/mcd19/11.png	Alolan Dugtrio	Metal
135	70	https://images.pokemontcg.io/ex10/8.png	Jolteon	Lightning
3	230	https://images.pokemontcg.io/xy12/2.png	M Venusaur-EX	Grass
127	120	https://images.pokemontcg.io/sm9/9.png	Pinsir	Grass
68	100	https://images.pokemontcg.io/base1/8.png	Machamp	Fighting
126	90	https://images.pokemontcg.io/sm115/10.png	Magmar	Fire
65	100	https://images.pokemontcg.io/ecard1/1.png	Alakazam	Psychic
36	70	https://images.pokemontcg.io/base2/1.png	Clefable	Colorless
1	70	https://images.pokemontcg.io/sm35/1.png	Bulbasaur	Grass
132	60	https://images.pokemontcg.io/ex6/4.png	Ditto	Colorless
82	60	https://images.pokemontcg.io/base1/9.png	Magneton	Lightning
151	50	https://images.pokemontcg.io/basep/8.png	Mew	Psychic
133	60	https://images.pokemontcg.io/mcd19/12.png	Eevee	Colorless
103	160	https://images.pokemontcg.io/sm6/2a.png	Alolan Exeggutor	Grass
54	60	https://images.pokemontcg.io/sm115/11.png	Psyduck	Water
5	70	https://images.pokemontcg.io/pop5/5.png	Charmeleon ╬┤	Lightning
6	180	https://images.pokemontcg.io/xy2/11.png	Charizard-EX	Fire
150	100	https://images.pokemontcg.io/ru1/9.png	Mewtwo	Psychic
2	100	https://images.pokemontcg.io/sm35/2.png	Ivysaur	Grass
80	60	https://images.pokemontcg.io/base6/8.png	Dark Slowbro	Psychic
101	90	https://images.pokemontcg.io/base2/2.png	Electrode	Lightning
97	80	https://images.pokemontcg.io/ecard2/H12.png	Hypno	Psychic
46	60	https://images.pokemontcg.io/swsh3/3.png	Paras	Grass
10	40	https://images.pokemontcg.io/sm10/2.png	Caterpie	Grass
123	70	https://images.pokemontcg.io/sm7/4.png	Scyther	Grass
13	50	https://images.pokemontcg.io/sm4/1.png	Weedle	Grass
134	60	https://images.pokemontcg.io/base6/9.png	Dark Vaporeon	Water
131	70	https://images.pokemontcg.io/si1/12.png	Lapras	Water
117	70	https://images.pokemontcg.io/gym1/9.png	Misty's Seadra	Water
4	50	https://images.pokemontcg.io/sm9/11.png	Charmander	Fire
146	120	https://images.pokemontcg.io/xy10/9.png	Moltres	Fire
128	70	https://images.pokemontcg.io/pop2/5.png	Tauros	Colorless
49	80	https://images.pokemontcg.io/hgss4/11.png	Venomoth	Grass
112	90	https://images.pokemontcg.io/ex4/11.png	Team Magma's Rhydon	Fighting,Darkness
25	50	https://images.pokemontcg.io/pop6/9.png	Pikachu	Lightning
26	80	https://images.pokemontcg.io/gym2/11.png	Lt. Surge's Raichu	Lightning
69	50	https://images.pokemontcg.io/xy3/1.png	Bellsprout	Grass
79	70	https://images.pokemontcg.io/sm115/12.png	Slowpoke	Water
55	70	https://images.pokemontcg.io/gym2/12.png	Misty's Golduck	Water
136	70	https://images.pokemontcg.io/base6/10.png	Flareon	Fire
144	100	https://images.pokemontcg.io/dp5/1.png	Articuno	Water
34	90	https://images.pokemontcg.io/base4/11.png	Nidoking	Grass
58	80	https://images.pokemontcg.io/bw4/10.png	Growlithe	Fire
115	80	https://images.pokemontcg.io/ex6/6.png	Kangaskhan	Colorless
37	60	https://images.pokemontcg.io/mcd16/1.png	Vulpix	Fire
14	80	https://images.pokemontcg.io/sm4/2.png	Kakuna	Grass
70	80	https://images.pokemontcg.io/xy3/2.png	Weepinbell	Grass
47	120	https://images.pokemontcg.io/swsh3/4.png	Parasect	Grass
141	100	https://images.pokemontcg.io/ex13/9.png	Kabutops ╬┤	Lightning
94	80	https://images.pokemontcg.io/base6/11.png	Gengar	Psychic
120	50	https://images.pokemontcg.io/sm115/13.png	Staryu	Water
107	60	https://images.pokemontcg.io/gym1/11.png	Rocket's Hitmonchan	Fighting
149	140	https://images.pokemontcg.io/dp6/2.png	Dragonite	Colorless
59	100	https://images.pokemontcg.io/neo4/12.png	Light Arcanine	Fire
50	30	https://images.pokemontcg.io/ru1/11.png	Diglett	Fighting
62	120	https://images.pokemontcg.io/ex10/11.png	Poliwrath	Fighting
143	190	https://images.pokemontcg.io/smp/SM05.png	Snorlax-GX	Colorless
77	60	https://images.pokemontcg.io/xy2/14.png	Ponyta	Fire
24	70	https://images.pokemontcg.io/ecard1/3.png	Arbok	Grass
31	90	https://images.pokemontcg.io/base4/12.png	Nidoqueen	Grass
78	90	https://images.pokemontcg.io/xy2/15.png	Rapidash	Fire
38	80	https://images.pokemontcg.io/base1/12.png	Ninetales	Fire
121	190	https://images.pokemontcg.io/sm115/14.png	Starmie-GX	Water
9	100	https://images.pokemontcg.io/ecard1/4.png	Blastoise	Water
43	50	https://images.pokemontcg.io/sm10/5.png	Oddish	Grass
8	60	https://images.pokemontcg.io/si1/15.png	Wartortle	Water
106	60	https://images.pokemontcg.io/base6/13.png	Hitmonlee	Fighting
7	50	https://images.pokemontcg.io/bw10/14.png	Squirtle	Water
102	50	https://images.pokemontcg.io/sm4/4.png	Exeggcute	Grass
142	80	https://images.pokemontcg.io/pl4/13.png	Aerodactyl	Fighting
129	30	https://images.pokemontcg.io/sm115/15.png	Magikarp	Water
52	50	https://images.pokemontcg.io/basep/10.png	Meowth	Colorless
108	70	https://images.pokemontcg.io/si1/16.png	Lickitung	Colorless
16	40	https://images.pokemontcg.io/pop4/12.png	Pidgey	Colorless
18	80	https://images.pokemontcg.io/base4/14.png	Pidgeot	Colorless
44	80	https://images.pokemontcg.io/sm10/7.png	Gloom	Grass
45	70	https://images.pokemontcg.io/si1/17.png	Vileplume	Grass
139	110	https://images.pokemontcg.io/ex13/13.png	Omastar ╬┤	Psychic
57	60	https://images.pokemontcg.io/si1/18.png	Primeape	Fighting
85	80	https://images.pokemontcg.io/hgss3/11.png	Dodrio	Colorless
19	30	https://images.pokemontcg.io/ru1/15.png	Rattata	Colorless
98	70	https://images.pokemontcg.io/xy4/13.png	Krabby	Water
86	80	https://images.pokemontcg.io/xy10/15.png	Seel	Water
148	70	https://images.pokemontcg.io/ex3/14.png	Dragonair	Colorless
114	80	https://images.pokemontcg.io/xy12/8.png	Tangela	Grass
88	70	https://images.pokemontcg.io/dc1/7.png	Team Aqua's Grimer	Psychic
48	60	https://images.pokemontcg.io/sm10/9.png	Venonat	Grass
99	100	https://images.pokemontcg.io/xy4/14.png	Kingler	Water
81	40	https://images.pokemontcg.io/neo2/7.png	Magnemite	Metal
40	90	https://images.pokemontcg.io/ex14/13.png	Wigglytuff	Colorless
116	40	https://images.pokemontcg.io/sm75/15.png	Horsea	Water
20	70	https://images.pokemontcg.io/ex7/17.png	Dark Raticate	Darkness
110	60	https://images.pokemontcg.io/base5/14.png	Dark Weezing	Grass
91	80	https://images.pokemontcg.io/ecard1/8.png	Cloyster	Water
28	70	https://images.pokemontcg.io/ex7/18.png	Dark Sandslash	Fighting,Darkness
83	60	https://images.pokemontcg.io/hgss1/19.png	Farfetch'd	Colorless
138	80	https://images.pokemontcg.io/xy10/17.png	Omanyte	Water
137	50	https://images.pokemontcg.io/basep/15.png	Cool Porygon	Colorless
27	60	https://images.pokemontcg.io/sm2/19.png	Alolan Sandshrew	Water
100	50	https://images.pokemontcg.io/sm115/21.png	Voltorb	Lightning
93	50	https://images.pokemontcg.io/base3/21.png	Haunter	Psychic
113	90	https://images.pokemontcg.io/ex10/20.png	Chansey	Colorless
53	60	https://images.pokemontcg.io/basep/17.png	Dark Persian	Colorless
76	140	https://images.pokemontcg.io/pl4/19.png	Golem	Fighting
125	70	https://images.pokemontcg.io/base1/20.png	Electabuzz	Lightning
95	70	https://images.pokemontcg.io/gym1/21.png	Brock's Onix	Fighting
66	70	https://images.pokemontcg.io/swsh35/24.png	Machop	Fighting
111	60	https://images.pokemontcg.io/gym1/22.png	Brock's Rhyhorn	Fighting
42	70	https://images.pokemontcg.io/ex8/31.png	Golbat	Grass
60	60	https://images.pokemontcg.io/sm1/30.png	Poliwag	Water
92	50	https://images.pokemontcg.io/base3/33.png	Gastly	Psychic
90	60	https://images.pokemontcg.io/xy9/22.png	Shellder	Water
118	60	https://images.pokemontcg.io/xy8/27.png	Goldeen	Water
124	70	https://images.pokemontcg.io/base1/31.png	Jynx	Psychic
75	70	https://images.pokemontcg.io/gym2/34.png	Brock's Graveler	Fighting
61	90	https://images.pokemontcg.io/sm1/31.png	Poliwhirl	Water
119	90	https://images.pokemontcg.io/xy8/28.png	Seaking	Water
74	70	https://images.pokemontcg.io/sm115/33.png	Geodude	Fighting
23	70	https://images.pokemontcg.io/swsh35/33.png	Ekans	Darkness
72	40	https://images.pokemontcg.io/gym1/32.png	Misty's Tentacool	Water
64	60	https://images.pokemontcg.io/base1/32.png	Kadabra	Psychic
41	50	https://images.pokemontcg.io/xy4/31.png	Zubat	Psychic
140	60	https://images.pokemontcg.io/ex12/36.png	Kabuto	Fighting
67	80	https://images.pokemontcg.io/base1/34.png	Machoke	Fighting
147	60	https://images.pokemontcg.io/sm75/34.png	Dratini	Dragon
35	50	https://images.pokemontcg.io/neo1/30.png	Clefairy	Colorless
104	60	https://images.pokemontcg.io/sm115/37.png	Cubone	Fighting
96	50	https://images.pokemontcg.io/ex6/32.png	Drowzee	Psychic
33	60	https://images.pokemontcg.io/base1/37.png	Nidorino	Grass
17	60	https://images.pokemontcg.io/base6/34.png	Pidgeotto	Colorless
32	60	https://images.pokemontcg.io/xy11/43.png	Nidoran ÔÖé	Psychic
30	80	https://images.pokemontcg.io/gym2/44.png	Giovanni's Nidorina	Grass
29	60	https://images.pokemontcg.io/bw9/40.png	Nidoran ÔÖÇ	Psychic
56	50	https://images.pokemontcg.io/ex6/38.png	Mankey	Fighting
63	30	https://images.pokemontcg.io/base1/43.png	Abra	Psychic
84	40	https://images.pokemontcg.io/ex9/45.png	Doduo	Colorless
109	50	https://images.pokemontcg.io/gym2/48.png	Koga's Koffing	Grass
21	50	https://images.pokemontcg.io/hgss2/62.png	Spearow	Colorless
\.


--
-- Name: cards cards_pkey; Type: CONSTRAINT; Schema: public; Owner: admin
--

ALTER TABLE ONLY public.cards
    ADD CONSTRAINT cards_pkey PRIMARY KEY (pokedex_number);


--
-- PostgreSQL database dump complete
--

\unrestrict 4TKg6ok2nDaBk3vOyylWLq1YA15DZfc4cQljnvn8MXUz7PIVPMylC3JaUPSvdWY

