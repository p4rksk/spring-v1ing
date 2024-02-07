package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final HttpSession session;

    private final BoardRepository boardRepository;


    @PostMapping("/board/{id}/update")
    public String update(@PathVariable int id, BoardRequest.UpdateDTO requestDTO){
        //인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");


        //권한 체크
        Board board = boardRepository.findById(id);

        if (board == null){
            return "error/403";
        }
        if (board.getUserId() != sessionUser.getId()){
            return "error/403";
        }

        //핵심 로직
        //update board_tb set title = ?, content = ? where id = ?;
        boardRepository.update(requestDTO, id);

        return "redirect:/board/"+id;

    }


    //게시글 수정 화면
    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id, HttpServletRequest request){
        //인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        //2/권한 체크
        Board board = boardRepository.findById(id);
        if (board.getUserId() != sessionUser.getId()){
            return "error/403";
        }

        //3.가방에 담기
        request.setAttribute("board",board);



        return "board/updateForm";
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable int id, HttpServletRequest request){
        // 1. 인증 안되면 겟 아웃
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null){ //401
            return "redirect:/loginForm";
        }

        //2.권한 없으면 나가
        Board board = boardRepository.findById(id);
        if (board.getUserId() != sessionUser.getId()){
            request.setAttribute("status",403);
            request.setAttribute("msg","게시글을 삭제할 권한이 없습니다.");
            return "error/40x";
        }

        boardRepository.deleteById(id);


        return "redirect:/";
    }

    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO requestDTO, HttpServletRequest request){
        //1. 인증체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }



        //2. 바디 데이터 확인 및 유효성 검사
        System.out.println(requestDTO);

        if (requestDTO.getTitle().length() > 30){
            request.setAttribute("status",400);
            request.setAttribute("msg","title의 길이가 30자를 초과해서는 안됩니다.");
            return "error/40x"; //BadRequest
        }


        //3.모델 위임
        //insert into board_tb(title, content, user_id, created_at) values(?,?,?, now));
        boardRepository.save(requestDTO, sessionUser.getId());



        return "redirect:/";
    }

    @GetMapping({"/", "/board"})
    public String index(HttpServletRequest request) {

        List<Board> boardList = boardRepository.findAll();
        request.setAttribute("boardList", boardList);

        return "index";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        // session 영역 키값의 user 객체 있는지 확인하기(인증)
        User sessionUser = (User) session.getAttribute("sessionUser");
        //        값이 null이면 로그인 페이지로 redirection
        // 값이 null이 아니면 /board/saveForm으로 이동
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        return "board/saveForm";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, HttpServletRequest request) {
       //1. 모델 진입 - 상세보기 데이터 가져오기
        // 바디 데이터가 없으면 유효성 검사가 필요없다
        BoardResponse.DetailDTO responseDTO = boardRepository.findByIdWithUser(id);



      //2. 페이지 주인 여부 체크(board의 userId와 sessionUser의 id를 비교)
        boolean pageOwner;

        User sessionUser = (User) session.getAttribute("sessionUser");//세션에 저장 돼있는 user객체 가져오기

        int boardID = responseDTO.getUserId();
        int UserId = sessionUser.getId();

        if (sessionUser == null){
            pageOwner=false;
        }else{
            pageOwner = boardID == UserId;
        }
        request.setAttribute("board", responseDTO);
        request.setAttribute("pageOwner", pageOwner);
        return "board/detail";
    }
}
