import '../../styles.css'

export const currentGameCard = (userName) => {
    return `
        <div class="flex flex-col content-center bg-white/10 w-full pl-10 pr-10 pt-6 pb-6 rounded-3xl shadow-md backdrop-blur-[1.5px] max-w-96">
            <img width="128" height="128" class="self-center" src="../../../public/assets/astronaut.png" alt="Astronaut">
            <h3 class="text-[1.25rem] font-bold text-[#0A3D62] mt-4 block">${userName}</h3>
            <button id="joinGamebtn" class="
            bg-[#27AE60] 
            cursor-pointer 
            h-11 
            font-['League_Spartan',_sans-serif] 
            text-[1rem] 
            text-base 
            font-bold 
            text-white 
            border-0 
            rounded-3xl 
            px-6 py-2 
            w-full 
            leading-normal">UNIRSE</button>
        </div>
    `
}