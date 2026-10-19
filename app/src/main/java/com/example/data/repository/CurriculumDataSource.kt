package com.example.data.repository

import com.example.data.model.Lesson
import com.example.data.model.LessonWord
import com.example.data.model.PronunciationExercise
import com.example.data.model.QuizQuestion
import com.example.data.model.StoryParagraph

object CurriculumDataSource {

    val lessons: List<Lesson> = listOf(
        Lesson(
            id = "lesson_a1_morning",
            title = "A Fresh Morning in Seattle",
            level = "A1",
            category = "Cotidiano",
            estimatedMinutes = 6,
            xpReward = 30,
            storyTitle = "Elena's Morning Routine",
            storyIntroduction = "Aprende vocabulario cotidiano y estructuras básicas para describir tu día.",
            paragraphs = listOf(
                StoryParagraph(
                    englishText = "Elena wakes up at seven o'clock every morning. The sun shines brightly through her bedroom window.",
                    spanishText = "Elena se despierta a las siete en punto cada mañana. El sol brilla intensamente a través de la ventana de su habitación."
                ),
                StoryParagraph(
                    englishText = "She drinks a warm cup of coffee and prepares fresh toast with avocado. It is a peaceful routine.",
                    spanishText = "Ella toma una taza de café caliente y prepara tostadas frescas con aguacate. Es una rutina pacífica."
                ),
                StoryParagraph(
                    englishText = "Before leaving her apartment, she checks the weather forecast. She grabs her warm coat and walks to the subway station.",
                    spanishText = "Antes de salir de su apartamento, revisa el pronóstico del tiempo. Toma su abrigo abrigado y camina hacia la estación del metro."
                ),
                StoryParagraph(
                    englishText = "On the train, she enjoys reading an interesting book about architecture. Learning new things makes her very happy.",
                    spanishText = "En el tren, disfruta leer un libro interesante sobre arquitectura. Aprender cosas nuevas la hace muy feliz."
                )
            ),
            vocabulary = listOf(
                LessonWord("brightly", "/ˈbraɪt.li/", "Adverbio", "Brillantemente, intensamente", "The stars shine brightly at night.", "Las estrellas brillan intensamente por la noche."),
                LessonWord("peaceful", "/ˈpiːs.fəl/", "Adjetivo", "Pacífico, tranquilo", "Sunday mornings are very peaceful.", "Las mañanas de domingo son muy tranquilas."),
                LessonWord("forecast", "/ˈfɔːr.kæst/", "Sustantivo", "Pronóstico del tiempo", "The weather forecast predicts rain.", "El pronóstico del clima predice lluvia."),
                LessonWord("subway", "/ˈsʌb.weɪ/", "Sustantivo", "Metro / Tren subterráneo", "We take the subway to work.", "Tomamos el metro al trabajo."),
                LessonWord("routine", "/ruːˈtiːn/", "Sustantivo", "Rutina diaria", "A morning routine boosts energy.", "Una rutina matutina aumenta la energía.")
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = "q_a1_1",
                    question = "What time does Elena wake up every morning?",
                    options = listOf("At six o'clock", "At seven o'clock", "At eight o'clock", "At nine o'clock"),
                    correctIndex = 1,
                    explanation = "The story states: 'Elena wakes up at seven o'clock every morning.'"
                ),
                QuizQuestion(
                    id = "q_a1_2",
                    question = "What does Elena drink in the morning?",
                    options = listOf("Cold orange juice", "Green tea", "A warm cup of coffee", "Hot chocolate"),
                    correctIndex = 2,
                    explanation = "She drinks a warm cup of coffee and prepares fresh toast."
                ),
                QuizQuestion(
                    id = "q_a1_3",
                    question = "How does she travel to her destination?",
                    options = listOf("By subway train", "By bicycle", "By driving her car", "By walking all the way"),
                    correctIndex = 0,
                    explanation = "She grabs her coat and walks to the subway station, taking the train."
                )
            )
        ),
        Lesson(
            id = "lesson_a2_cafe",
            title = "Ordering Coffee & Friends",
            level = "A2",
            category = "Viajes",
            estimatedMinutes = 7,
            xpReward = 35,
            storyTitle = "The Cozy Corner Café",
            storyIntroduction = "Domina el lenguaje social para pedir alimentos, interactuar con el barista y conversar.",
            paragraphs = listOf(
                StoryParagraph(
                    englishText = "Liam and Sophia meet at The Cozy Corner Café on Friday afternoon. The place smells delightfully of roasted beans and vanilla.",
                    spanishText = "Liam y Sophia se encuentran en The Cozy Corner Café el viernes por la tarde. El lugar huele deliciosamente a granos tostados y vainilla."
                ),
                StoryParagraph(
                    englishText = "\"Good afternoon! What can I get started for you today?\" asks the friendly barista behind the wooden counter.",
                    spanishText = "\"¡Buenas tardes! ¿Qué puedo ofrecerles para comenzar hoy?\" pregunta el amable barista detrás del mostrador de madera."
                ),
                StoryParagraph(
                    englishText = "\"I would like an iced oat latte with a hint of cinnamon, please,\" Sophia says with a warm smile.",
                    spanishText = "\"Me gustaría un café con leche de avena helado con un toque de canela, por favor,\" dice Sophia con una cálida sonrisa."
                ),
                StoryParagraph(
                    englishText = "Liam orders a double espresso and asks if the blueberry muffins are baked fresh today. The barista nods enthusiastically.",
                    spanishText = "Liam pide un espresso doble y pregunta si los muffins de arándanos están recién horneados hoy. El barista asiente con entusiasmo."
                ),
                StoryParagraph(
                    englishText = "They sit by the large glass window, laughing and planning their upcoming weekend road trip to the mountains.",
                    spanishText = "Se sientan junto al gran ventanal de vidrio, riendo y planeando su próximo viaje por carretera de fin de semana a las montañas."
                )
            ),
            vocabulary = listOf(
                LessonWord("delightfully", "/dɪˈlaɪt.fəl.i/", "Adverbio", "Deliciosamente / Con deleite", "The garden smells delightfully fresh.", "El jardín huele deliciosamente fresco."),
                LessonWord("counter", "/ˈkaʊn.tər/", "Sustantivo", "Mostrador / Barra", "Place your tray on the counter.", "Coloca tu bandeja en el mostrador."),
                LessonWord("enthusiastically", "/ɪnˌθuː.ziˈæs.tɪ.kəl.i/", "Adverbio", "Con entusiasmo", "She agreed enthusiastically.", "Ella aceptó con entusiasmo."),
                LessonWord("upcoming", "/ˈʌpˌkʌm.ɪŋ/", "Adjetivo", "Próximo / Venidero", "Get ready for the upcoming semester.", "Prepárate para el próximo semestre."),
                LessonWord("roasted", "/ˈroʊ.stɪd/", "Adjetivo", "Tostado / Asado", "I love the aroma of roasted coffee.", "Me encanta el aroma del café tostado.")
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = "q_a2_1",
                    question = "What kind of milk does Sophia choose for her iced latte?",
                    options = listOf("Almond milk", "Oat milk", "Soy milk", "Whole milk"),
                    correctIndex = 1,
                    explanation = "Sophia requests: 'an iced oat latte with a hint of cinnamon'."
                ),
                QuizQuestion(
                    id = "q_a2_2",
                    question = "What pastry does Liam ask about?",
                    options = listOf("Chocolate croissant", "Blueberry muffins", "Apple pie", "Cinnamon rolls"),
                    correctIndex = 1,
                    explanation = "Liam asks if the blueberry muffins are baked fresh today."
                ),
                QuizQuestion(
                    id = "q_a2_3",
                    question = "Where do Liam and Sophia sit to chat?",
                    options = listOf("On the outdoor patio", "Near the kitchen door", "By the large glass window", "At the corner bar"),
                    correctIndex = 2,
                    explanation = "They sit by the large glass window to plan their trip."
                )
            )
        ),
        Lesson(
            id = "lesson_b1_tech",
            title = "The Evolution of Smart Cities",
            level = "B1",
            category = "Ciencia",
            estimatedMinutes = 8,
            xpReward = 45,
            storyTitle = "How Technology Shapes Tomorrow",
            storyIntroduction = "Lectura intermedia sobre sostenibilidad, sensores inteligentes e innovación urbana.",
            paragraphs = listOf(
                StoryParagraph(
                    englishText = "Across the globe, metropolitan areas are transforming into intelligent ecosystems powered by renewable energy and data analysis.",
                    spanishText = "En todo el mundo, las áreas metropolitanas se están transformando en ecosistemas inteligentes impulsados por energía renovable y análisis de datos."
                ),
                StoryParagraph(
                    englishText = "Smart traffic signals dynamically adapt in real-time to ease congestion and reduce toxic vehicle emissions during peak commuting hours.",
                    spanishText = "Los semáforos inteligentes se adaptan dinámicamente en tiempo real para aliviar la congestión y reducir las emisiones tóxicas de vehículos en horas pico."
                ),
                StoryParagraph(
                    englishText = "Furthermore, urban gardens installed on high-rise rooftops provide organic produce while absorbing excess heat from concrete structures.",
                    spanishText = "Además, los huertos urbanos instalados en azoteas de rascacielos proporcionan productos orgánicos mientras absorben el exceso de calor de las estructuras de concreto."
                ),
                StoryParagraph(
                    englishText = "Citizens actively participate through mobile applications, reporting potholes or suggesting community improvements with a single tap.",
                    spanishText = "Los ciudadanos participan activamente a través de aplicaciones móviles, reportando baches o sugiriendo mejoras comunitarias con un solo toque."
                ),
                StoryParagraph(
                    englishText = "This remarkable harmony between cutting-edge innovation and environmental stewardship proves that modern cities can be both efficient and green.",
                    spanishText = "Esta notable armonía entre innovación de vanguardia y cuidado ambiental demuestra que las ciudades modernas pueden ser tanto eficientes como ecológicas."
                )
            ),
            vocabulary = listOf(
                LessonWord("ecosystem", "/ˈiː.koʊˌsɪs.təm/", "Sustantivo", "Ecosistema", "A vibrant digital ecosystem fosters ideas.", "Un ecosistema digital vibrante fomenta ideas."),
                LessonWord("congestion", "/kənˈdʒes.tʃən/", "Sustantivo", "Congestión vehicular / Atasco", "Carpooling helps relieve highway congestion.", "Compartir vehículo ayuda a aliviar la congestión."),
                LessonWord("furthermore", "/ˌfɜːr.ðərˈmɔːr/", "Conector", "Además / Asimismo", "Furthermore, the initiative saves public funds.", "Además, la iniciativa ahorra fondos públicos."),
                LessonWord("stewardship", "/ˈstjuː.ərd.ʃɪp/", "Sustantivo", "Mayordomía / Cuidado responsable", "Environmental stewardship is everyone's duty.", "El cuidado ambiental es deber de todos."),
                LessonWord("cutting-edge", "/ˌkʌt.ɪŋˈedʒ/", "Adjetivo", "De vanguardia / Puntero", "They employ cutting-edge renewable technology.", "Emplean tecnología renovable de vanguardia.")
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = "q_b1_1",
                    question = "How do smart traffic lights help cities during rush hour?",
                    options = listOf(
                        "They shut down power to conserve electricity",
                        "They adjust dynamically to ease congestion and lower emissions",
                        "They ticket speeding drivers automatically",
                        "They change color randomly"
                    ),
                    correctIndex = 1,
                    explanation = "Smart signals adapt in real-time to alleviate traffic congestion and reduce vehicle emissions."
                ),
                QuizQuestion(
                    id = "q_b1_2",
                    question = "What dual benefit do rooftop gardens offer?",
                    options = listOf(
                        "Produce organic food and absorb excess urban heat",
                        "Provide landing pads for drones and collect rainwater only",
                        "Replace public parks and increase rent prices",
                        "Attract birds and block mobile reception"
                    ),
                    correctIndex = 0,
                    explanation = "Urban gardens provide organic produce and absorb heat from concrete structures."
                ),
                QuizQuestion(
                    id = "q_b1_3",
                    question = "How do citizens contribute directly to smart city improvements?",
                    options = listOf(
                        "By working at municipal offices",
                        "By reporting issues like potholes via mobile apps",
                        "By building their own roads",
                        "By paying extra municipal fees"
                    ),
                    correctIndex = 1,
                    explanation = "Residents report infrastructure problems and suggest improvements through mobile apps."
                )
            )
        ),
        Lesson(
            id = "lesson_b2_leadership",
            title = "Cross-Cultural Communication",
            level = "B2",
            category = "Trabajo",
            estimatedMinutes = 10,
            xpReward = 55,
            storyTitle = "Navigating Global Team Collaboration",
            storyIntroduction = "Perfecciona tu comprensión auditiva y lectora avanzada con expresiones idiomáticas y matices profesionales.",
            paragraphs = listOf(
                StoryParagraph(
                    englishText = "In today's interconnected professional world, navigating subtle cultural nuances is paramount for collaborative success.",
                    spanishText = "En el mundo profesional interconectado de hoy, navegar los sutiles matices culturales es fundamental para el éxito colaborativo."
                ),
                StoryParagraph(
                    englishText = "While some cultures value direct, unequivocal feedback, others emphasize indirect diplomacy to preserve workplace harmony and camaraderie.",
                    spanishText = "Mientras que algunas culturas valoran la retroalimentación directa e inequívoca, otras enfatizan la diplomacia indirecta para preservar la armonía y el compañerismo laboral."
                ),
                StoryParagraph(
                    englishText = "Effective leaders actively cultivate empathetic listening. They discern not only the spoken words, but also non-verbal cues and underlying hesitations.",
                    spanishText = "Los líderes eficaces cultivan activamente la escucha empática. Disciernen no solo las palabras habladas, sino también las señales no verbales y las vacilaciones subyacentes."
                ),
                StoryParagraph(
                    englishText = "When diverse perspectives converge constructively, organizations foster unprecedented innovation, dismantling cognitive biases.",
                    spanishText = "Cuando diversas perspectivas convergen constructivamente, las organizaciones fomentan una innovación sin precedentes, desmantelando sesgos cognitivos."
                ),
                StoryParagraph(
                    englishText = "Ultimately, true fluency goes beyond grammatical proficiency; it bridges divergent worldviews through mutual respect.",
                    spanishText = "En última instancia, la verdadera fluidez va más allá del dominio gramatical; une cosmovisiones divergentes a través del respeto mutuo."
                )
            ),
            vocabulary = listOf(
                LessonWord("paramount", "/ˈpær.ə.maʊnt/", "Adjetivo", "De suma importancia / Primordial", "Clear communication is of paramount importance.", "La comunicación clara es de suma importancia."),
                LessonWord("unequivocal", "/ˌʌn.ɪˈkwɪv.ə.kəl/", "Adjetivo", "Inequívoco / Contundente", "Her statement gave an unequivocal answer.", "Su declaración dio una respuesta inequívoca."),
                LessonWord("discern", "/dɪˈsɜːrn/", "Verbo", "Discernir / Percibir con claridad", "A skilled mentor can discern subtle feelings.", "Un mentor hábil puede discernir sentimientos sutiles."),
                LessonWord("camaraderie", "/ˌkæm.əˈrɑː.dɚ.i/", "Sustantivo", "Camaradería / Compañerismo", "Team bonding builds great camaraderie.", "La convivencia de equipo crea gran camaradería."),
                LessonWord("proficiency", "/prəˈfɪʃ.ən.si/", "Sustantivo", "Dominio / Competencia técnica", "She reached advanced language proficiency.", "Ella alcanzó un dominio avanzado del idioma.")
            ),
            quizQuestions = listOf(
                QuizQuestion(
                    id = "q_b2_1",
                    question = "What contrast is highlighted between different workplace cultures?",
                    options = listOf(
                        "Working overtime versus working remotely",
                        "Direct feedback versus indirect diplomatic communication",
                        "Using email versus telephone calls",
                        "Formal attire versus casual dress"
                    ),
                    correctIndex = 1,
                    explanation = "The passage discusses how some cultures favor direct feedback while others practice indirect diplomacy."
                ),
                QuizQuestion(
                    id = "q_b2_2",
                    question = "What does empathetic listening require according to the text?",
                    options = listOf(
                        "Agreeing with every suggestion immediately",
                        "Taking extensive shorthand notes during meetings",
                        "Discerning both spoken words and non-verbal cues",
                        "Dominating conversations with strong opinions"
                    ),
                    correctIndex = 2,
                    explanation = "Empathetic leaders perceive spoken words alongside non-verbal cues and subtle hesitations."
                ),
                QuizQuestion(
                    id = "q_b2_3",
                    question = "What represents 'true fluency' according to the conclusion?",
                    options = listOf(
                        "Memorizing complex grammatical rules without speaking",
                        "Bridging divergent worldviews through mutual respect",
                        "Achieving native speed in two weeks",
                        "Eliminating every trace of a foreign accent"
                    ),
                    correctIndex = 1,
                    explanation = "True fluency transcends grammar; it bridges divergent worldviews through mutual respect."
                )
            )
        )
    )

    val pronunciationExercises: List<PronunciationExercise> = listOf(
        // Category: Everyday Essentials
        PronunciationExercise(
            id = "pronun_1",
            phrase = "Nice to meet you, I am learning English.",
            phonetic = "/naɪs tuː miːt juː, aɪ æm ˈlɜːr.nɪŋ ˈɪŋ.ɡlɪʃ/",
            translation = "Mucho gusto en conocerte, estoy aprendiendo inglés.",
            level = "A1",
            category = "Frases Esenciales",
            pronunciationTip = "Une 'meet' y 'you' suavemente como 'meet-choo'. La vocal en 'learn' es neutra y alargada."
        ),
        PronunciationExercise(
            id = "pronun_2",
            phrase = "Could you please speak a little slower?",
            phonetic = "/kʊd juː pliːz spiːk ə ˈlɪt.əl ˈsloʊ.ər/",
            translation = "¿Podrías hablar un poco más despacio, por favor?",
            level = "A1",
            category = "Frases Esenciales",
            pronunciationTip = "En 'Could', la 'l' es muda. No digas 'culd', pronuncia 'kud'."
        ),
        PronunciationExercise(
            id = "pronun_3",
            phrase = "I would like to order a fresh cup of coffee.",
            phonetic = "/aɪ wʊd laɪk tuː ˈɔːr.dər ə freʃ kʌp ʌv ˈkɔː.fi/",
            translation = "Me gustaría pedir una taza de café recién hecho.",
            level = "A1",
            category = "Viajes y Restaurantes",
            pronunciationTip = "En 'would', la 'l' es silenciosa. Conecta 'cup of' como 'kʌ-pəv'."
        ),
        PronunciationExercise(
            id = "pronun_4",
            phrase = "Where is the nearest subway station?",
            phonetic = "/wer ɪz ðə ˈnɪr.ɪst ˈsʌb.weɪ ˈsteɪ.ʃən/",
            translation = "¿Dónde está la estación de metro más cercana?",
            level = "A2",
            category = "Viajes y Restaurantes",
            pronunciationTip = "Pronuncia el 'th' en 'the' con la punta de la lengua entre los dientes vibrando suavemente."
        ),
        // Phoneme Challenge: TH sound
        PronunciationExercise(
            id = "pronun_5",
            phrase = "Think about three things that inspire you.",
            phonetic = "/θɪŋk əˈbaʊt θriː θɪŋz ðæt ɪnˈspaɪər juː/",
            translation = "Piensa en tres cosas que te inspiren.",
            level = "A2",
            category = "Reto Fonético: Sonido TH",
            pronunciationTip = "En 'Think' y 'Three', sopla aire sin vibrar cuerdas vocales (sonido sordo /θ/). ¡No lo cambies por 'F' o 'S'!"
        ),
        PronunciationExercise(
            id = "pronun_6",
            phrase = "They thought that the weather was healthy.",
            phonetic = "/ðeɪ θɔːt ðæt ðə ˈweð.ər wʌz ˈhel.θi/",
            translation = "Ellos pensaron que el clima era saludable.",
            level = "B1",
            category = "Reto Fonético: Sonido TH",
            pronunciationTip = "Alterna entre 'th' sonoro en 'They/weather' (/ð/) y sordo en 'thought/healthy' (/θ/)."
        ),
        // Phoneme Challenge: Short I vs Long EE
        PronunciationExercise(
            id = "pronun_7",
            phrase = "Please sit on this seat and relax a bit.",
            phonetic = "/pliːz sɪt ɑːn ðɪs siːt ænd rɪˈlæks ə bɪt/",
            translation = "Por favor siéntate en este asiento y relájate un poco.",
            level = "A2",
            category = "Vocales Cortas vs Largas",
            pronunciationTip = "Contrasta 'sit' (vocal corta y relajada /ɪ/) frente a 'seat' (vocal estirada y sonriente /iː/)."
        ),
        PronunciationExercise(
            id = "pronun_8",
            phrase = "I really appreciate your thoughtful guidance.",
            phonetic = "/aɪ ˈriː.ə.li əˈpriː.ʃi.eɪt jʊr ˈθɔːt.fəl ˈɡaɪ.dəns/",
            translation = "Realmente aprecio tu considerada orientación.",
            level = "B1",
            category = "Negocios y Fluidez",
            pronunciationTip = "La terminación '-ate' en 'appreciate' suena /eɪt/. Mantén un ritmo cadencioso."
        ),
        // Advanced Tongue Twisters
        PronunciationExercise(
            id = "pronun_9",
            phrase = "She sells seashells by the seashore smoothly.",
            phonetic = "/ʃiː sɛlz ˈsiːˌʃɛlz baɪ ðə ˈsiːˌʃɔːr ˈsmuːð.li/",
            translation = "Ella vende conchas marinas junto a la orilla del mar suavemente.",
            level = "B2",
            category = "Trabalenguas y Agilidad",
            pronunciationTip = "Distingue claramente entre el sonido 'sh' (/ʃ/) y 's' (/s/) sin mezclar la posición de la lengua."
        ),
        PronunciationExercise(
            id = "pronun_10",
            phrase = "Red lorry, yellow lorry, rolling down the road.",
            phonetic = "/rɛd ˈlɔːr.i, ˈjɛl.oʊ ˈlɔːr.i, ˈroʊ.lɪŋ daʊn ðə roʊd/",
            translation = "Camión rojo, camión amarillo, rodando por el camino.",
            level = "B2",
            category = "Trabalenguas y Agilidad",
            pronunciationTip = "Control preciso de la transición entre la 'R' inglesa (lengua curvada hacia atrás) y la 'L'."
        )
    )
}
