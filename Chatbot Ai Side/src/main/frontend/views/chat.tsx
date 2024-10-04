import { useState, useRef, useEffect } from "react";
import { ChatAiService } from "Frontend/generated/endpoints";
import Markdown from "react-markdown";
import '../styles/styles.css';

export default function Chat() {
    const [question, setQuestion] = useState<string>("");
    const [messages, setMessages] = useState<{ text: string; type: 'user' | 'ai'; date: string }[]>([]);
    const chatEndRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        chatEndRef.current?.scrollIntoView({ behavior: 'smooth' });
        createBubbles(); // Create bubbles on message update
    }, [messages]);

    async function send() {
        if (!question.trim()) return;

        const now = new Date().toLocaleString();
        try {
            setMessages(prevMessages => [...prevMessages, { text: question, type: 'user', date: now }]);

            const response = await ChatAiService.ragChat(question);
            setMessages(prevMessages => [...prevMessages, { text: response, type: 'ai', date: now }]);
            setQuestion("");
        } catch (error) {
            console.error("Error fetching response:", error);
        }
    }

    function clearChat() {
        const responseArea = document.querySelector('.response-area');

        if (responseArea) {
            responseArea.classList.add('clear-animation');

            setTimeout(() => {
                setMessages([]);
                responseArea.classList.remove('clear-animation');
            }, 300);
        }
    }

    function createBubbles() {
        const responseArea = document.querySelector('.response-area');
        if (!responseArea) return;

        // Clear existing bubbles
        const existingBubbles = responseArea.querySelectorAll('.bubble');
        existingBubbles.forEach(bubble => bubble.remove());

        // Create new bubbles
        const bubbleCount = 10; // Adjust the number of bubbles
        for (let i = 0; i < bubbleCount; i++) {
            const bubble = document.createElement('div');
            bubble.className = 'bubble';
            bubble.style.width = `${Math.random() * 30 + 20}px`; // Random size between 20px and 50px
            bubble.style.height = bubble.style.width; // Make it circular
            bubble.style.left = `${Math.random() * 100}%`; // Random horizontal position
            bubble.style.top = `${Math.random() * 100}%`; // Random vertical position
            bubble.style.animationDelay = `${Math.random() * 2}s`; // Random delay for animation

            responseArea.appendChild(bubble);
        }
    }

    return (
        <div className="chatbox">
            <h3>Chatbot AI Vinci</h3>
            <div className="response-area">
                {messages.length === 0 ? (
                    <div className="empty-message">Start a conversation by typing a question.</div>
                ) : (
                    messages.map((message, index) => (
                        <div key={index} className={`message ${message.type}`}>
                            <span className="message-text">
                                <Markdown>{message.text}</Markdown>
                            </span>
                            <span className="message-date">{message.date}</span>
                        </div>
                    ))
                )}
                <div ref={chatEndRef} />
            </div>
            <div className="input-area">
                <input
                    type="text"
                    placeholder="Ask something..."
                    value={question}
                    onChange={(e) => setQuestion(e.target.value)}
                />
                <button className="btn send-btn" onClick={send}>Send</button>
            </div>
            <button className="btn clear-btn" onClick={clearChat}>Clear Chat</button>
        </div>
    );
}
