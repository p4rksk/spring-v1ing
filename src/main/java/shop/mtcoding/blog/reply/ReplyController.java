package shop.mtcoding.blog.reply;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog.user.User;

//댓글 쓰기, 댓글 삭제. 댓글 목록보기
@RequiredArgsConstructor
@Controller
public class ReplyController {

        private final HttpSession session;
        private final ReplyRepository replyRepository;

        @PostMapping("/reply/save")
        public String write(ReplyRequest.WriteDTO requestDTO) {
                System.out.println(requestDTO);
                // 1. 인증 안되면 겟 아웃
                User sessionUser = (User) session.getAttribute("sessionUser");
                System.out.println("2222");
                if (sessionUser == null) { //401
                        return "redirect:/user/loginForm";
                }

                //유효성 검사(추후 만들어야됨)

                // 핵심코드
                replyRepository.save(requestDTO, sessionUser.getId());

                return "redirect:/board/"+requestDTO.getBoardId();

        }
}

